-- charger
insert into charger (created, charge_point_id)
    values (now()::timestamp, 'charger');

-- ocpp_token
insert into ocpp_token (created, id_tag, max_active_tx_count)
    values (now()::timestamp, '1234567890', 1);
insert into ocpp_token (created, id_tag, max_active_tx_count)
    values (now()::timestamp, '2345678901', 1);
