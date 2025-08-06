package controller_applicativi;

import bean.BookingBean;
import dao.*;
import entity.*;
import facade.ApplicationFacade;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.*;

public class BookingService {

    private static final Logger LOG = Logger.getLogger(BookingService.class.getName());

    private final GenericDao<Booking>       bookingDao      = DaoFactory.getInstance().getBookingDao();
    private final GenericDao<Room>          roomDao         = DaoFactory.getInstance().getRoomDao();
    private final GenericDao<Activity>      activityDao     = DaoFactory.getInstance().getActivityDao();
    private final GenericDao<CruisePackage> packageDao      = DaoFactory.getInstance().getCruisePackageDao();
    private final GenericDao<Destination>   destinationDao  = DaoFactory.getInstance().getDestinationDao();

   
    public String book(BookingBean bean) {

        /* ---------------- VALIDAZIONE  ---------------- */
        if (bean.getFirstName() == null || bean.getFirstName().isBlank()
            || bean.getLastName()  == null || bean.getLastName().isBlank()
            || bean.getEmail()     == null || bean.getEmail().isBlank()
            || bean.getPhoneNumber()== null || bean.getPhoneNumber().isBlank()
            || bean.getRoomType()  == null || bean.getDestinationName() == null
            || bean.getPackageName()== null || bean.getActivityName() == null)
        {
            return "error:validation";
        }

        Room room;
        try {
            room = roomDao.read(bean.getRoomType());
        } catch (SQLException ex) {                  
            LOG.log(Level.SEVERE, "Errore DB durante lookup stanza", ex);
            return "error:db";                        
        }

        if (room == null || room.getStatus() != RoomStatus.FREE) {
            return "error:validation";
        }

        
        Booking bk = new Booking();
        bk.setFirstName(bean.getFirstName());
        bk.setLastName(bean.getLastName());
        bk.setEmail(bean.getEmail());
        bk.setPhoneNumber(bean.getPhoneNumber());

        bk.setRoomType(bean.getRoomType());
        bk.setDestinationName(bean.getDestinationName());
        bk.setPackageName(bean.getPackageName());
        bk.setActivityName(bean.getActivityName());

        

        try {
            bookingDao.create(bk);
           
            room.setStatus(RoomStatus.PENDING);
            roomDao.update(room);

           
            ApplicationFacade.sendBookingReceivedEmail(bk);

            return "success";

        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "DB error durante insert booking", ex);
            return "error:db";
        }
    }

    
    /** Tipi di stanza attualmente FREE (solo i nomi). */
    public List<String> getFreeRoomTypes() {
        return roomDao.readAll().stream()
                      .filter(r -> r.getStatus() == RoomStatus.FREE)
                      .map(Room::getType)
                      .toList();
    }

    /** Nomi delle attività disponibili. */
    public List<String> getActivityNames() {
        return activityDao.readAll().stream().map(Activity::getName).toList();
    }

    /** Nomi dei pacchetti disponibili. */
    public List<String> getPackageNames() {
        return packageDao.readAll().stream().map(CruisePackage::getName).toList();
    }

    /** Nomi delle destinazioni disponibili. */
    public List<String> getDestinationNames() {
        return destinationDao.readAll().stream().map(Destination::getName).toList();
    }
}