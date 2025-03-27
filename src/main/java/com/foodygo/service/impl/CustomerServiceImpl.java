package com.foodygo.service.impl;

import com.foodygo.configuration.CustomUserDetail;
import com.foodygo.dto.BuildingDTO;
import com.foodygo.dto.CustomerDTO;
import com.foodygo.dto.UserDTO;
import com.foodygo.dto.request.CustomerCreateRequest;
import com.foodygo.dto.request.CustomerUpdateRequest;
import com.foodygo.dto.response.PagingResponse;
import com.foodygo.entity.*;
import com.foodygo.exception.AuthenticationException;
import com.foodygo.exception.ElementExistException;
import com.foodygo.exception.ElementNotFoundException;
import com.foodygo.exception.UnchangedStateException;
import com.foodygo.mapper.BuildingMapper;
import com.foodygo.mapper.CustomerMapper;
import com.foodygo.mapper.UserMapper;
import com.foodygo.repository.CustomerRepository;
import com.foodygo.repository.UserRepository;
import com.foodygo.service.spec.BuildingService;
import com.foodygo.service.spec.CustomerService;
import com.foodygo.service.spec.S3Service;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CustomerServiceImpl extends BaseServiceImpl<Customer, Integer> implements CustomerService {

    private final CustomerRepository customerRepository;
    private final BuildingService buildingService;
    private final UserRepository userRepository;
    private final CustomerMapper customerMapper;
    private final UserMapper userMapper;
    private final BuildingMapper buildingMapper;
    private final S3Service s3Service;

    // Firebase

    @Value("${firebase.bucket.name}")
    private String bucketName;

    @Value("${firebase.content.type}")
    private String contentType;

    @Value("${firebase.get.stream}")
    private String fileConfigFirebase;

    @Value("${firebase.get.url}")
    private String urlFirebase;

    @Value("${firebase.get.folder}")
    private String folderContainImage;

    @Value("${firebase.file.format}")
    private String fileFormat;

    // BufferImage

    @Value("${buffer-image.type}")
    private String bufferImageType;

    @Value("${buffer-image.fill-rect.width}")
    private int bufferImageWidth;

    @Value("${buffer-image.fill-rect.height}")
    private int bufferImageHeight;

    @Value("${buffer-image.fill-rect.color.background}")
    private String bufferImageColorBackground;

    @Value("${buffer-image.fill-rect.color.text}")
    private String bufferImageColorText;

    @Value("${buffer-image.fill-rect.font.text}")
    private String bufferImageFontText;

    @Value("${buffer-image.fill-rect.size.text}")
    private int bufferImageSizeText;

    @Value("${buffer-image.fill-rect.x}")
    private int bufferImageX;

    @Value("${buffer-image.fill-rect.y}")
    private int bufferImageY;

    @Value("${buffer-image.devide}")
    private int bufferImageDevide;

    public CustomerServiceImpl(CustomerRepository customerRepository, BuildingService buildingService, UserRepository userRepository,
                               CustomerMapper customerMapper, UserMapper userMapper, BuildingMapper buildingMapper, S3Service s3Service) {
        super(customerRepository);
        this.customerRepository = customerRepository;
        this.buildingService = buildingService;
        this.userRepository = userRepository;
        this.customerMapper = customerMapper;
        this.userMapper = userMapper;
        this.buildingMapper = buildingMapper;
        this.s3Service = s3Service;
    }

    @Override
    public PagingResponse getAllCustomers(Integer currentPage, Integer pageSize) {

        Pageable pageable = PageRequest.of(currentPage - 1, pageSize);

        var pageData = customerRepository.findAll(pageable);

        return !pageData.getContent().isEmpty() ? PagingResponse.builder()
                .code("Success")
                .message("Get all customers paging successfully")
                .currentPage(currentPage)
                .pageSizes(pageSize)
                .totalElements(pageData.getTotalElements())
                .totalPages(pageData.getTotalPages())
                .data(pageData.getContent().stream()
                        .map(customerMapper::customerToCustomerDTO)
                        .toList())
                .build() :
                PagingResponse.builder()
                        .code("Failed")
                        .message("Get all customers paging failed")
                        .currentPage(currentPage)
                        .pageSizes(pageSize)
                        .totalElements(pageData.getTotalElements())
                        .totalPages(pageData.getTotalPages())
                        .data(pageData.getContent().stream()
                                .map(customerMapper::customerToCustomerDTO)
                                .toList())
                        .build();
    }

    @Override
    public PagingResponse getAllCustomersActive(Integer currentPage, Integer pageSize) {
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize);

        var pageData = customerRepository.findAllByDeletedFalse(pageable);

        return !pageData.getContent().isEmpty() ? PagingResponse.builder()
                .code("Success")
                .message("Get all customers active paging successfully")
                .currentPage(currentPage)
                .pageSizes(pageSize)
                .totalElements(pageData.getTotalElements())
                .totalPages(pageData.getTotalPages())
                .data(pageData.getContent().stream()
                        .map(customerMapper::customerToCustomerDTO)
                        .toList())
                .build() :
                PagingResponse.builder()
                        .code("Failed")
                        .message("Get all customers active paging failed")
                        .currentPage(currentPage)
                        .pageSizes(pageSize)
                        .totalElements(pageData.getTotalElements())
                        .totalPages(pageData.getTotalPages())
                        .data(pageData.getContent().stream()
                                .map(customerMapper::customerToCustomerDTO)
                                .toList())
                        .build();
    }

    @Override
    public List<CustomerDTO> getCustomers() {
        return customerRepository.findByDeletedIsFalse().stream().map(customerMapper::customerToCustomerDTO).collect(Collectors.toList());
    }

    @Override
    public CustomerDTO undeleteCustomer(Integer customerID) {
        Customer customer = customerRepository.findCustomerById((customerID));
        if (customer == null) {
            throw new ElementNotFoundException("Customer not found");
        }
        if (!customer.isDeleted()) {
            throw new UnchangedStateException("Customer is not deleted");
        }
        customer.setDeleted(false);
        return customerMapper.customerToCustomerDTO(customerRepository.save(customer));
    }

    @Override
    public UserDTO getUserByCustomerID(Integer customerID) {
        Customer customer = customerRepository.findCustomerById(customerID);
        if (customer == null) {
            throw new ElementNotFoundException("Customer not found");
        }
        return userMapper.userToUserDTO(customer.getUser());
    }

    private Customer getCustomer(Integer customerID) {
        Customer customer = customerRepository.findCustomerById(customerID);
        if (customer == null) {
            throw new ElementNotFoundException("Customer not found");
        }
        return customer;
    }

    private Building getBuilding(int buildingID) {
        Building building = buildingService.findById(buildingID);
        if (building == null) {
            throw new ElementNotFoundException("Building is not found");
        }
        return building;
    }

    private User getUser(int userID) {
        User user = userRepository.getUserByUserID(userID);
        if (user == null) {
            throw new ElementNotFoundException("User is not found");
        }
        return user;
    }

    @Override
    public CustomerDTO createCustomer(CustomerCreateRequest customerCreateRequest) {
        // Allow null building create request because of Google Registration
        Building building = null;
        if (customerCreateRequest.getBuildingID() != null) {
            building = getBuilding(customerCreateRequest.getBuildingID());
        }
        User user = getUser(customerCreateRequest.getUserID());
        try {
            String url = null;
            // Khong lay duoc quyen "Invalid JWT Signature"
//            if(customerCreateRequest.getImage() == null) {
//                String dataUrl = generateImageWithInitial(user.getEmail());
//                url = uploadFileBase64(dataUrl);
//            } else {
//                url = upload(customerCreateRequest.getImage());
//            }
            Customer customer = Customer.builder()
                    .image(customerCreateRequest.getImage())
                    .building(building)
                    .user(user)
                    .build();
            return customerMapper.customerToCustomerDTO(customerRepository.save(customer));
        } catch (Exception e) {
            log.error("Customer creation failed", e);
            return null;
        }
    }

    @Override
    public UserDTO updateCustomer(CustomerUpdateRequest customerUpdateRequest, int userId) {

        User user = getUser(userId);
        if (user.getCustomer() == null) {
            throw new ElementNotFoundException("Customer profile not found");
        }

        CustomUserDetail customUserDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (customUserDetail.getUserID() != user.getUserID()) {
            throw new AuthenticationException("You are not allowed to update other customer");
        }

        if (customerUpdateRequest.getEmail() != null) {
            if (!user.getEmail().equals(customerUpdateRequest.getEmail().trim())) {
                if (userRepository.getUserByEmail(customerUpdateRequest.getEmail()) != null) {
                    throw new ElementExistException("Email đã tồn tại");
                }
                user.setEmail(customerUpdateRequest.getEmail());
            }
        }
        if (customerUpdateRequest.getPhone() != null) {
            if (user.getPhone() != null) {
                if (!user.getPhone().equals(customerUpdateRequest.getPhone().trim())) {
                    if (userRepository.getUserByPhone(customerUpdateRequest.getPhone()) != null) {
                        throw new ElementExistException("Phone number đã tồn tại");
                    }
                }
            }
            user.setPhone(customerUpdateRequest.getPhone());
        }

        if (customerUpdateRequest.getFullName() != null) user.setFullName(customerUpdateRequest.getFullName().trim());
        if (customerUpdateRequest.getDob() != null) user.setDob(customerUpdateRequest.getDob());

        Customer customer = user.getCustomer();
        customer.setUser(user);
        if (customerUpdateRequest.getImage() != null) {
            String url = s3Service.uploadFileToS3(customerUpdateRequest.getImage(), "userImage");
            customer.setImage(url);
        }

        if (customerUpdateRequest.getBuildingID() != null) {
            Building building = getBuilding(customerUpdateRequest.getBuildingID());
            customer.setBuilding(building);
        }

        userRepository.save(user);
        customerRepository.save(customer);
        UserDTO userDTO = userMapper.userToUserDTO(user);
        userDTO = userDTO.toBuilder()
                .buildingID(customer.getBuilding() != null ? customer.getBuilding().getId() : null)
                .buildingName(customer.getBuilding() != null ? customer.getBuilding().getName() : null)
                .image(customer.getImage() != null ? customer.getImage() : null)
                .build();
        return userDTO;
    }

    @Override
    public List<Order> getOrdersByCustomerID(Integer customerID) {
        // chua có order service
        return List.of();
    }

    @Override
    public CustomerDTO getCustomerByOrderID(Integer orderID) {
        // chua có order service
        return null;
    }

    @Override
    public BuildingDTO getBuildingByCustomerID(int customerID) {
        Customer customer = getCustomer(customerID);
        return buildingMapper.buildingToBuildingDTO(customer.getBuilding());
    }

    @Override
    public UserDTO getUserByCustomerID(int customerID) {
        Customer customer = getCustomer(customerID);
        return userMapper.userToUserDTO(customer.getUser());
    }

    @Override
    public Wallet getWalletByCustomerID(Integer customerID) {
        Customer customer = getCustomer(customerID);
        return customer.getWallet();
    }

    @Override
    public CustomerDTO getCustomerByWalletID(Integer walletID) {
        // chưa có wallet service
        return null;
    }

    @Override
    public CustomerDTO deleteCustomer(Integer customerID) {
        Customer customer = customerRepository.findCustomerById(customerID);
        if (customer == null) {
            throw new ElementNotFoundException("Customer not found");
        }
        if (customer.isDeleted()) {
            throw new UnchangedStateException("Customer is not deleted");
        }
        User user = customer.getUser();
        if (user != null) {
            user.setDeleted(true);
            user.setEnabled(false);
            user.setNonLocked(false);
        }
        customer.setDeleted(true);
        userRepository.save(user);
        return customerMapper.customerToCustomerDTO(customerRepository.save(customer));
    }

//    @Override
//    public List<Deposit> getDepositByCustomerID(Integer customerID) {
//        Customer customer = getCustomer(customerID);
//        if (customer != null) {
//            return customer.getDeposit();
//        }
//        return null;
//    }
//
//    @Override
//    public Customer getCustomerByDepositID(Integer depositID) {
//        // chưa có wallet service
//        return null;
//    }


    //  Xử lí hình ảnh vs firebase

    private String uploadFile(File file, String fileName) throws IOException {  // file vs fileName is equal
        String folder = folderContainImage + "/" + fileName;  // 1 is folder and fileName is "randomString + "extension""
        BlobId blobId = BlobId.of(bucketName, folder); // blodId is a path to file in firebase
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(contentType).build();  // blodInfo contains blodID and more
        InputStream inputStream = CustomerServiceImpl.class.getClassLoader().getResourceAsStream(fileConfigFirebase); // change the file name with your one
        Credentials credentials = GoogleCredentials.fromStream(inputStream);
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        storage.create(blobInfo, Files.readAllBytes(file.toPath()));
        // saved image on firebase
        String DOWNLOAD_URL = urlFirebase;
        return String.format(DOWNLOAD_URL, URLEncoder.encode(folder, StandardCharsets.UTF_8));
    }

    private boolean deleteImageOnFireBase(String urlImage) throws IOException {
        String folder = folderContainImage + "/" + urlImage;
        BlobId blobId = BlobId.of(bucketName, folder); // Replace with your bucker name
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(contentType).build();
        InputStream inputStream = CustomerServiceImpl.class.getClassLoader().getResourceAsStream(fileConfigFirebase); // change the file name with your one
        Credentials credentials = GoogleCredentials.fromStream(inputStream);
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        return storage.delete(blobId);
    }

    private String uploadFileBase64(String base64Image) throws IOException {

        String fileName = UUID.randomUUID().toString() + fileFormat;  // Generate a random file name
        String folder = folderContainImage + "/" + fileName;
        BlobId blobId = BlobId.of(bucketName, folder); // Replace with your bucket name
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(contentType).build();    // type media does not see the picture on firebase

        InputStream inputStream = CustomerServiceImpl.class.getClassLoader().getResourceAsStream(fileConfigFirebase); // change the file name with your one
        Credentials credentials = GoogleCredentials.fromStream(inputStream);
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();

        byte[] imageBytes = Base64.getDecoder().decode(base64Image);
        storage.create(blobInfo, imageBytes);

        String DOWNLOAD_URL = urlFirebase;
        return String.format(DOWNLOAD_URL, URLEncoder.encode(folder, StandardCharsets.UTF_8));
    }

    private File convertToFile(MultipartFile multipartFile, String fileName) throws IOException {
        File tempFile = new File(fileName);          // create newFile ưith String of fileName (random String + "extension") and save to Current Working Directory or Java Virtual Machine (JVM)
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(multipartFile.getBytes());
        }
        return tempFile;
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    private String upload(MultipartFile multipartFile) {
        try {
            String fileName = multipartFile.getOriginalFilename();                        // to get file name.jpg, .png, ...
            fileName = UUID.randomUUID().toString().concat(this.getExtension(fileName));  // to generated random string values for file name and plus + "extension".
            File file1 = this.convertToFile(multipartFile, fileName);                      // to convert multipartFile to File
            String URL = this.uploadFile(file1, fileName);                                   // to get uploaded file link
            file1.delete();
            return URL;
        } catch (Exception e) {
            return "Image couldn't upload, Something went wrong";
        }
    }

    private String upload(MultipartFile[] multipartFile) {
        try {

            for (MultipartFile file : multipartFile) {
                String fileName = file.getOriginalFilename();                        // to get original file name
                fileName = UUID.randomUUID().toString().concat(this.getExtension(fileName));  // to generated random string values for file name.

                File file1 = this.convertToFile(file, fileName);                      // to convert multipartFile to File
                String URL = this.uploadFile(file1, fileName);                                   // to get uploaded file link
                file1.delete();
            }
//            String fileName = multipartFile.getOriginalFilename();                        // to get original file name
//            fileName = UUID.randomUUID().toString().concat(this.getExtension(fileName));  // to generated random string values for file name.
//
//            File file = this.convertToFile(multipartFile, fileName);                      // to convert multipartFile to File
//            String URL = this.uploadFile(file, fileName);                                   // to get uploaded file link
//            file.delete();
//            return URL;
        } catch (Exception e) {
            return "Image couldn't upload, Something went wrong";
        }
        return null;
    }

    private String generateImageWithInitial(String userName) throws IOException {

        char initial = Character.toUpperCase(userName.trim().charAt(0));

        // create width and height of image
        int width = bufferImageWidth;
        int height = bufferImageHeight;

        // BufferedImage to process image in memory, it can be drawing, edit, insert things into image
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // insert character into image
        Graphics2D graphics = bufferedImage.createGraphics();
        graphics.setColor(Color.decode("#" + bufferImageColorBackground));
        graphics.fillRect(bufferImageX, bufferImageY, width, height);  // x, y is the conner on the top left of rectangle
        graphics.setFont(new Font(bufferImageFontText, Font.BOLD, bufferImageSizeText));
        graphics.setColor(Color.decode("#" + bufferImageColorText));
        FontMetrics fontMetrics = graphics.getFontMetrics();
        int x = (width - fontMetrics.charWidth(initial)) / bufferImageDevide;
        int y = ((height - fontMetrics.getHeight()) / bufferImageDevide) + fontMetrics.getAscent();
        graphics.drawString(String.valueOf(initial), x, y);
        graphics.dispose();

        // change image to byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, bufferImageType, baos);

        byte[] imageBytes = baos.toByteArray();

        // Encode the byte array to a Base64 string
        String base64String = Base64.getEncoder().encodeToString(imageBytes);

        return base64String;
    }

}
