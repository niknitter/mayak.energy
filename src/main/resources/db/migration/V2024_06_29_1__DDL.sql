-- charger
create table charger (
  id bigserial not null,
  created timestamp not null,
  charge_point_id varchar(255) not null,
  charge_point_vendor varchar(255),
  charge_point_model varchar(255),
  charge_point_serial_number varchar(255),
  charge_box_serial_number varchar(255),
  last_heartbeat_moment timestamp,
  constraint charger_pk primary key (id),
  constraint charger_uq_charge_point_id unique (charge_point_id)
);

comment on table charger is 'Зарядная станция';
comment on column charger.id is 'ID';
comment on column charger.created is 'Время создания записи (системное)';
comment on column charger.charge_point_id is 'Идентификатор станции (chargePointId)';
comment on column charger.charge_point_vendor is 'Проиводитель станции';
comment on column charger.charge_point_model is 'Модель станции';
comment on column charger.charge_point_serial_number is 'Серийный номер станции';
comment on column charger.charge_box_serial_number is 'Серийный номер (box) станции';
comment on column charger.last_heartbeat_moment is 'Время последнего heartbeat';

-- connector
create table connector (
  id bigserial not null,
  created timestamp not null,
  charger_id bigint not null,
  connector_id integer not null,
  status_utc timestamp,
  status varchar(255),
  error_code varchar(255),
  constraint connector_pk primary key (id),
  constraint connector_uq_charger_id_connector_id unique (charger_id, connector_id),
  constraint connector_fk_charger_id foreign key (charger_id) references charger (id)
);
create index connector_idx_charger_id on connector (charger_id);

comment on table connector is 'Коннектор зарядной станции';
comment on column connector.id is 'ID';
comment on column connector.created is 'Время создания записи (системное)';
comment on column connector.charger_id is 'ID зарядной станции';
comment on column connector.connector_id is 'Идентификатор коннектора (OCPP)';
comment on column connector.status_utc is 'Время UTC статуса коннектора';
comment on column connector.status is 'Статус (OCPP)';
comment on column connector.error_code is 'Код ошибки';

-- connector_status
create table connector_status (
  id bigserial not null,
  created timestamp not null,
  connector_id bigint not null,
  status_utc timestamp not null,
  status varchar(255) not null,
  error_code varchar(255) not null,
  vendor_id varchar(255),
  vendor_error_code varchar(255),
  constraint connector_status_pk primary key (id),
  constraint connector_status_fk_connector_id foreign key (connector_id) references connector (id)
);
create index connector_status_idx_connector_id on connector_status (connector_id);
create index connector_status_idx_connector_id_status_utc on connector_status (connector_id, status_utc);

comment on table connector_status is 'Статус коннектора зарядной станции (история)';
comment on column connector_status.id is 'ID';
comment on column connector_status.created is 'Время создания записи (системное)';
comment on column connector_status.connector_id is 'ID коннектора зарядной станции';
comment on column connector_status.status_utc is 'Время UTC статуса коннектора';
comment on column connector_status.status is 'Статус (OCPP)';
comment on column connector_status.error_code is 'Код ошибки';
comment on column connector_status.vendor_id is 'Информация поставщика';
comment on column connector_status.vendor_error_code is 'Код ошибки поставщика';

-- ocpp_token
create table ocpp_token (
  id bigserial not null,
  created timestamp not null,
  id_tag varchar(255) not null,
  max_active_tx_count integer not null,
  parent_id bigint,
  expired timestamp,
  blocked timestamp,
  constraint ocpp_token_pk primary key (id),
  constraint ocpp_token_uq_id_token unique (id_tag),
  constraint ocpp_token_fk_parent_id foreign key (parent_id) references ocpp_token (id)
);
create index ocpp_token_idx_parent_id on ocpp_token (parent_id);

comment on table ocpp_token is 'Токен доступа';
comment on column ocpp_token.id is 'ID';
comment on column ocpp_token.created is 'Время создания записи (системное)';
comment on column ocpp_token.id_tag is 'UID токена (RFID-карты)';
comment on column ocpp_token.parent_id is 'ID родительского токена';
comment on column ocpp_token.max_active_tx_count is 'Максимальное число активных транзакций (0 - ограничения нет)';
comment on column ocpp_token.expired is 'Время истечения срока действия (локальное)';
comment on column ocpp_token.blocked is 'Время блокировки (локальное)';

-- ocpp_transaction
create table ocpp_transaction (
  id bigserial not null,
  created timestamp not null,
  connector_id bigint not null,
  token_id bigint not null,
  start_utc timestamp not null,
  start_value integer not null,
  stop_utc timestamp,
  stop_value integer,
  stop_reason varchar(255),
  constraint ocpp_transaction_pk primary key (id),
  constraint ocpp_transaction_uq_connector_id_token_id_start_utc_start_value unique (connector_id, token_id, start_utc, start_value),
  constraint ocpp_transaction_fk_connector_id foreign key (connector_id) references connector (id),
  constraint ocpp_transaction_fk_token_id foreign key (token_id) references ocpp_token (id)
);
create index ocpp_transaction_idx_connector_id on ocpp_transaction (connector_id);
create index ocpp_transaction_idx_token_id on ocpp_transaction (token_id);

comment on table ocpp_transaction is 'Транзакция';
comment on column ocpp_transaction.id is 'ID';
comment on column ocpp_transaction.created is 'Время создания записи (системное)';
comment on column ocpp_transaction.connector_id is 'ID коннектора зарядной станции';
comment on column ocpp_transaction.token_id is 'ID токена';
comment on column ocpp_transaction.start_utc is 'Время UTC старта транзакции';
comment on column ocpp_transaction.start_value is 'Начальное значение счетчика';
comment on column ocpp_transaction.stop_utc is 'Время UTC окончания транзакции';
comment on column ocpp_transaction.stop_value is 'Конечное значение счетчика';
comment on column ocpp_transaction.stop_reason is 'Причина остановки (OCPP)';

-- connector_value
create table connector_value (
  id bigserial not null,
  created timestamp not null,
  connector_id bigint not null,
  transaction_id bigint,
  value_utc timestamp not null,
  value varchar(255) not null,
  context varchar(255),
  format varchar(255),
  measurand varchar(255),
  phase varchar(255),
  location varchar(255),
  unit varchar(255),
  constraint connector_value_pk primary key (id),
  constraint connector_value_fk_connector_id foreign key (connector_id) references connector (id),
  constraint connector_value_fk_ocpp_trancation_id foreign key (transaction_id) references ocpp_transaction (id)
);
create index connector_value_idx_connector_id on connector_value (connector_id);
create index connector_value_idx_transaction_id on connector_value (transaction_id);

comment on table connector_value is 'Значение коннектора';
comment on column connector_value.id is 'ID';
comment on column connector_value.created is 'Время создания записи (системное)';
comment on column connector_value.connector_id is 'ID коннектора зарядной станции';
comment on column connector_value.transaction_id is 'ID транзакции';
comment on column connector_value.value_utc is 'Время UTC значения';
comment on column connector_value.value is 'Значение';
comment on column connector_value.context is 'Контекст';
comment on column connector_value.format is 'Формат';
comment on column connector_value.measurand is 'Измеряемая величина';
comment on column connector_value.phase is 'Фаза';
comment on column connector_value.location is 'Расположение';
comment on column connector_value.unit is 'Единица измерения';
