package dao;



import entity.*;

public class DaoFactory implements DaoFactoryInterface {

    /* ---------- singleton ---------- */
    private static final DaoFactory INSTANCE = new DaoFactory();

    public static DaoFactory getInstance() {
        return INSTANCE;
    }

    /* ---------- configurazione ---------- */
    public enum Store {DATABASE, FILE, STATELESS}

    private static Store storageOption = Store.STATELESS;      // default

    public static void setStorageOption(Store opt) {
        storageOption = opt;
    }

    /* ---------- cache DAO in-memory ---------- */
    private static UserDaoMemory userDaoMemoryInstance;
    private static StafDaoMemory stafDaoMemoryInstance;
    private static BookingDaoMemory bookingDaoMemoryInstance;
    
    
    private static GenericDao<Activity> activityDaoMemoryInstance;
   
    private static ReviewDaoMemory           reviewDaoMemoryInstance;
    private static GenericDao<Review>        reviewDaoFileInstance;
    private static RoomDaoMemory          roomDaoMemoryInstance;
    private static CruisePackageDaoMemory cruisePackageMemoryInstance;
    private static DestinationDaoMemory  destinationMemoryInstance;

    private static GenericDao<User> userDaoFileInstance;
    private static GenericDao<Staf> stafDaoFileInstance;
    private static GenericDao<Booking> bookingDaoFileInstance;
    
   
    private static GenericDao<Activity> activityDaoFileInstance;
    private static GenericDao<Room>          roomDaoFileInstance;
    private static GenericDao<CruisePackage> cruisePackageFileInstance;
    private static GenericDao<Destination>   destinationFileInstance;

    /* ---------- costruttore privato ---------- */
    public DaoFactory() {

        // Costruttore privato per nascondere quello pubblico implicito
    }

    /* ---------- DAO di istanza ---------- */
    public GenericDao<User> getUserDao() {
        return switch (storageOption) {
            case DATABASE -> new UserDaoDB(DatabaseConnectionManager.getConnection());
            case FILE -> getUserFileInstance();
            default -> getUserMemoryInstance();
        };
    }

    public GenericDao<Staf> getStafDao() {
        return switch (storageOption) {
            case DATABASE -> new StafDaoDB(DatabaseConnectionManager.getConnection());
            case FILE -> getStafFileInstance();
            default -> getStafMemoryInstance();
        };
    }

    private static GenericDao<User> getUserFileInstance() {
        if (userDaoFileInstance == null)
            userDaoFileInstance = new UserDaoFile();
        return userDaoFileInstance;
    }

    /* ---------- helper per cache ---------- */
    private static UserDaoMemory getUserMemoryInstance() {
        if (userDaoMemoryInstance == null)
            userDaoMemoryInstance = new UserDaoMemory();
        return userDaoMemoryInstance;
    }

    private static GenericDao<Staf> getStafFileInstance() {
        if (stafDaoFileInstance == null)
            stafDaoFileInstance = new StafDaoFile();
        return stafDaoFileInstance;
    }

    private static StafDaoMemory getStafMemoryInstance() {
        if (stafDaoMemoryInstance == null)
            stafDaoMemoryInstance = new StafDaoMemory();
        return stafDaoMemoryInstance;
    }

    public GenericDao<Booking> getBookingDao() {

        switch (storageOption) {

            case DATABASE -> {
                return new BookingDaoDB(DatabaseConnectionManager.getConnection());

            }

            case FILE -> {
                return getBookingFileInstance();
            }

            default -> {                              // STATELESS
                return getBookingMemoryInstance();
            }
        }
    }


    private static GenericDao<Booking> getBookingFileInstance() {
        if (bookingDaoFileInstance == null)
            bookingDaoFileInstance = new BookingDaoFile();
        return bookingDaoFileInstance;
    }

    /* ---- singleton in-memory --------------------------------- */
    private static BookingDaoMemory getBookingMemoryInstance() {
        if (bookingDaoMemoryInstance == null)
            bookingDaoMemoryInstance = new BookingDaoMemory();
        return bookingDaoMemoryInstance;
    }

    
    
    
    public GenericDao<Activity> getActivityDao() {
        return switch (storageOption) {
            case DATABASE -> new ActivityDaoDB(DatabaseConnectionManager.getConnection());
            case FILE -> getActivityFileInstance(); 
            default -> getActivityMemoryInstance();
        };
    }
    
    
    public GenericDao<Review> getReviewDao() {
        return switch (storageOption) {
            case DATABASE -> new ReviewDaoDB(DatabaseConnectionManager.getConnection());
            case FILE     -> getReviewFileInstance();
            default       -> getReviewMemoryInstance();
        };
    }
    private static GenericDao<Review> getReviewFileInstance() {
        if (reviewDaoFileInstance == null) reviewDaoFileInstance = new ReviewDaoFile();
        return reviewDaoFileInstance;
    }
    private static ReviewDaoMemory getReviewMemoryInstance() {
        if (reviewDaoMemoryInstance == null) reviewDaoMemoryInstance = new ReviewDaoMemory();
        return reviewDaoMemoryInstance;
    }
    
    

    public static GenericDao<Activity> getActivityFileInstance() {
        if (activityDaoFileInstance == null) {
            activityDaoFileInstance = new ActivityDaoFile();
        }
        return activityDaoFileInstance;
    }

    public static ActivityDaoMemory getActivityMemoryInstance(){
        if (activityDaoMemoryInstance == null) {
            activityDaoMemoryInstance = new ActivityDaoMemory();
        }
        return (ActivityDaoMemory) activityDaoMemoryInstance;
    }
    public GenericDao<Room> getRoomDao() {
        return switch (storageOption) {
            case DATABASE -> new RoomDaoDB(DatabaseConnectionManager.getConnection());
            case FILE     -> getRoomFileInstance();
            default       -> getRoomMemoryInstance();
        };
    }
    private static GenericDao<Room> getRoomFileInstance() {
        if (roomDaoFileInstance == null) roomDaoFileInstance = new RoomDaoFile();
        return roomDaoFileInstance;
    }
    private static RoomDaoMemory getRoomMemoryInstance() {
        if (roomDaoMemoryInstance == null) roomDaoMemoryInstance = new RoomDaoMemory();
        return roomDaoMemoryInstance;
    }

    /* =========================================================
     *                  CRUISE PACKAGE DAO
     * ========================================================= */
    public GenericDao<CruisePackage> getCruisePackageDao() {
        return switch (storageOption) {
            case DATABASE -> new CruisePackageDaoDB(DatabaseConnectionManager.getConnection());
            case FILE     -> getCruisePackageFileInstance();
            default       -> getCruisePackageMemoryInstance();
        };
    }
    private static GenericDao<CruisePackage> getCruisePackageFileInstance() {
        if (cruisePackageFileInstance == null)
            cruisePackageFileInstance = new CruisePackageDaoFile();
        return cruisePackageFileInstance;
    }
    private static CruisePackageDaoMemory getCruisePackageMemoryInstance() {
        if (cruisePackageMemoryInstance == null)
            cruisePackageMemoryInstance = new CruisePackageDaoMemory();
        return cruisePackageMemoryInstance;
    }

    /* =========================================================
     *                  DESTINATION DAO
     * ========================================================= */
    public GenericDao<Destination> getDestinationDao() {
        return switch (storageOption) {
            case DATABASE -> new DestinationDaoDB(DatabaseConnectionManager.getConnection());
            case FILE     -> getDestinationFileInstance();
            default       -> getDestinationMemoryInstance();
        };
    }
    private static GenericDao<Destination> getDestinationFileInstance() {
        if (destinationFileInstance == null)
            destinationFileInstance = new DestinationDaoFile();
        return destinationFileInstance;
    }
    private static DestinationDaoMemory getDestinationMemoryInstance() {
        if (destinationMemoryInstance == null)
            destinationMemoryInstance = new DestinationDaoMemory();
        return destinationMemoryInstance;
    }

}
