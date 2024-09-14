-- charger
/*insert into charger (created, charge_point_id)
    values (now()::timestamp, 'charger');*/

-- ocpp_token
insert into ocpp_token (created, id_tag, max_active_tx_count)
    values (now()::timestamp, 'idTag0', 10),
           (now()::timestamp, 'idTag1', 10),
           (now()::timestamp, 'idTag2', 10),
           (now()::timestamp, 'idTag3', 10),
           (now()::timestamp, 'idTag4', 10),
           (now()::timestamp, 'idTag5', 10),
           (now()::timestamp, 'idTag6', 10),
           (now()::timestamp, 'idTag7', 10),
           (now()::timestamp, 'idTag8', 10),
           (now()::timestamp, 'idTag9', 10);
