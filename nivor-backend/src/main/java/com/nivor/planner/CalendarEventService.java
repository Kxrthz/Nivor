package com.nivor.planner;
import com.nivor.common.exception.ResourceNotFoundException;import com.nivor.user.*;import com.nivor.planner.dto.CalendarEventRequest;import java.time.LocalDateTime;import java.util.List;import org.springframework.stereotype.Service;import org.springframework.transaction.annotation.Transactional;
@Service public class CalendarEventService{
 private final CalendarEventRepository events;private final UserService users;public CalendarEventService(CalendarEventRepository e,UserService u){events=e;users=u;}
 @Transactional(readOnly=true)public List<CalendarEvent> list(String email,LocalDateTime from,LocalDateTime to){Long uid=users.getByEmail(email).getId();if((from==null)!=(to==null))throw new IllegalArgumentException("Both from and to are required");if(from!=null&&to.isBefore(from))throw new IllegalArgumentException("to must not be before from");return from==null?events.findAllByUser_IdOrderByStartTimeAsc(uid):events.findAllByUser_IdAndStartTimeLessThanEqualAndEndTimeGreaterThanEqualOrderByStartTimeAsc(uid,to,from);}
 @Transactional(readOnly=true)public CalendarEvent get(String email,Long id){return owned(email,id);}
 @Transactional public CalendarEvent create(String email,CalendarEventRequest r){CalendarEvent e=new CalendarEvent();e.setUser(users.getByEmail(email));apply(e,r);return events.save(e);}
 @Transactional public CalendarEvent update(String email,Long id,CalendarEventRequest r){CalendarEvent e=owned(email,id);apply(e,r);return e;}
 @Transactional public void delete(String email,Long id){events.delete(owned(email,id));}
 private CalendarEvent owned(String email,Long id){return events.findByIdAndUser_Id(id,users.getByEmail(email).getId()).orElseThrow(()->new ResourceNotFoundException("Calendar event not found"));}
 private void apply(CalendarEvent e,CalendarEventRequest r){if(r.endTime().isBefore(r.startTime()))throw new IllegalArgumentException("endTime cannot be before startTime");e.setTitle(r.title().trim());e.setDescription(r.description());e.setStartTime(r.startTime());e.setEndTime(r.endTime());e.setLocation(r.location());e.setColor(r.color());e.setAllDay(r.allDay());e.setRecurrenceRule(r.recurrenceRule());}
}
