SLACK_EVENTS
id	
channel_key	
channel_name
created_by	
date
message		
name		
notified
event_type_id (Foreign Key)

EVENT_TYPE
id (PK)
name
description

EVENT_TYPE_PROPERTIES
id
event_type_id (Foreign Key)
start_date_time
end_date_time
notify_time_before
schedule_types [ONE_TIME, RECURRING]



