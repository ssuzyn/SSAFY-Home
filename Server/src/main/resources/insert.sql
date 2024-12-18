LOCK TABLES `members` WRITE;
/*!40000 ALTER TABLE `members` DISABLE KEYS */;
INSERT INTO members (user_id, user_name, user_pwd, email_id, email_domain, join_date, gender, age, phone_number)
VALUES
    ('boss', 'ZIP 보스', '1234', 'boss', 'gmail.com', '2023-10-08 16:10:08', 'M', 35, '010-1234-5678'),
    ('ssafy', '김싸피', '1234', 'ssafy', 'ssafy.com', '2024-11-25 22:45:01', 'F', 27, '010-9876-5432'),
    ('snoopy', '스누피', '1234', 'hello', 'naver.com', '2024-11-26 10:23:08', 'F', 20, '010-1111-2222'),
    ('dragon', '뱀의 꼬리', '1234', 'snake', 'gmail.com', '2024-11-26 14:05:15', 'M', 24, '010-3333-4444');
/*!40000 ALTER TABLE `members` ENABLE KEYS */;
UNLOCK TABLES;


LOCK TABLES `fileinfo` WRITE;
/*!40000 ALTER TABLE `fileinfo` DISABLE KEYS */;
INSERT INTO fileinfo (user_id, original_name, save_path)
VALUES
    ('boss', '1732590347026_zipLogo.png', 'boss/1732590347026_zipLogo.png'),
    ('ssafy', 'basic_profile.png', 'ssafy/basic_profile.png'),
    ('snoopy', 'snoopy.jpg', 'snoopy/1732604640492_snoopy.jpg'),
    ('dragon', 'dragon.png', 'dragon/1732605106527_dragon.png');
/*!40000 ALTER TABLE `fileinfo` ENABLE KEYS */;
UNLOCK TABLES;


LOCK TABLES `guguncodes` WRITE;
/*!40000 ALTER TABLE `guguncodes` DISABLE KEYS */;
INSERT INTO `guguncodes` VALUES ('1111000000','종로구'),('1114000000','중구'),('1117000000','용산구'),('1120000000','성동구'),('1121500000','광진구'),('1123000000','동대문구'),('1126000000','중랑구'),('1129000000','성북구'),('1130500000','강북구'),('1132000000','도봉구'),('1135000000','노원구'),('1138000000','은평구'),('1141000000','서대문구'),('1144000000','마포구'),('1147000000','양천구'),('1150000000','강서구'),('1153000000','구로구'),('1154500000','금천구'),('1156000000','영등포구'),('1159000000','동작구'),('1162000000','관악구'),('1165000000','서초구'),('1168000000','강남구'),('1171000000','송파구'),('1174000000','강동구'),('2611000000','중구'),('2614000000','서구'),('2617000000','동구'),('2620000000','영도구'),('2623000000','부산진구'),('2626000000','동래구'),('2629000000','남구'),('2632000000','북구'),('2635000000','해운대구'),('2638000000','사하구'),('2641000000','금정구'),('2644000000','강서구'),('2647000000','연제구'),('2650000000','수영구'),('2653000000','사상구'),('2671000000','기장군'),('2711000000','중구'),('2714000000','동구'),('2717000000','서구'),('2720000000','남구'),('2723000000','북구'),('2726000000','수성구'),('2729000000','달서구'),('2771000000','달성군'),('2811000000','중구'),('2814000000','동구'),('2817700000','미추홀구'),('2818500000','연수구'),('2820000000','남동구'),('2823700000','부평구'),('2824500000','계양구'),('2826000000','서구'),('2871000000','강화군'),('2872000000','옹진군'),('2911000000','동구'),('2914000000','서구'),('2915500000','남구'),('2917000000','북구'),('2920000000','광산구'),('3011000000','동구'),('3014000000','중구'),('3017000000','서구'),('3020000000','유성구'),('3023000000','대덕구'),('3111000000','중구'),('3114000000','남구'),('3117000000','동구'),('3120000000','북구'),('3171000000','울주군'),('3611000000','세종특별자치시'),('4111000000','수원시'),('4111100000','수원시 장안구'),('4111300000','수원시 권선구'),('4111500000','수원시 팔달구'),('4111700000','수원시 영통구'),('4113000000','성남시'),('4113100000','성남시 수정구'),('4113300000','성남시 중원구'),('4113500000','성남시 분당구'),('4115000000','의정부시'),('4117000000','안양시'),('4117100000','안양시 만안구'),('4117300000','안양시 동안구'),('4119000000','부천시'),('4121000000','광명시'),('4122000000','평택시'),('4125000000','동두천시'),('4127000000','안산시'),('4127100000','안산시 상록구'),('4127300000','안산시 단원구'),('4128000000','고양시'),('4128100000','고양시 덕양구'),('4128500000','고양시 일산동구'),('4128700000','고양시 일산서구'),('4129000000','과천시'),('4131000000','구리시'),('4136000000','남양주시'),('4137000000','오산시'),('4139000000','시흥시'),('4141000000','군포시'),('4143000000','의왕시'),('4145000000','하남시'),('4146000000','용인시'),('4146100000','용인시 처인구'),('4146300000','용인시 기흥구'),('4146500000','용인시 수지구'),('4148000000','파주시'),('4150000000','이천시'),('4155000000','안성시'),('4157000000','김포시'),('4159000000','화성시'),('4161000000','광주시'),('4163000000','양주시'),('4165000000','포천시'),('4167000000','여주시'),('4180000000','연천군'),('4182000000','가평군'),('4183000000','양평군'),('4211000000','춘천시'),('4213000000','원주시'),('4215000000','강릉시'),('4217000000','동해시'),('4219000000','태백시'),('4221000000','속초시'),('4223000000','삼척시'),('4272000000','홍천군'),('4273000000','횡성군'),('4275000000','영월군'),('4276000000','평창군'),('4277000000','정선군'),('4278000000','철원군'),('4279000000','화천군'),('4280000000','양구군'),('4281000000','인제군'),('4282000000','고성군'),('4283000000','양양군'),('4311000000','청주시'),('4311100000','청주시 상당구'),('4311200000','청주시 서원구'),('4311300000','청주시 흥덕구'),('4311400000','청주시 청원구'),('4313000000','충주시'),('4315000000','제천시'),('4372000000','보은군'),('4373000000','옥천군'),('4374000000','영동군'),('4374500000','증평군'),('4375000000','진천군'),('4376000000','괴산군'),('4377000000','음성군'),('4380000000','단양군'),('4413000000','천안시'),('4413100000','천안시 동남구'),('4413300000','천안시 서북구'),('4415000000','공주시'),('4418000000','보령시'),('4420000000','아산시'),('4421000000','서산시'),('4423000000','논산시'),('4425000000','계룡시'),('4427000000','당진시'),('4471000000','금산군'),('4476000000','부여군'),('4477000000','서천군'),('4479000000','청양군'),('4480000000','홍성군'),('4481000000','예산군'),('4482500000','태안군'),('4511000000','전주시'),('4511100000','전주시 완산구'),('4511300000','전주시 덕진구'),('4513000000','군산시'),('4514000000','익산시'),('4518000000','정읍시'),('4519000000','남원시'),('4521000000','김제시'),('4571000000','완주군'),('4572000000','진안군'),('4573000000','무주군'),('4574000000','장수군'),('4575000000','임실군'),('4577000000','순창군'),('4579000000','고창군'),('4580000000','부안군'),('4611000000','목포시'),('4613000000','여수시'),('4615000000','순천시'),('4617000000','나주시'),('4623000000','광양시'),('4671000000','담양군'),('4672000000','곡성군'),('4673000000','구례군'),('4677000000','고흥군'),('4678000000','보성군'),('4679000000','화순군'),('4680000000','장흥군'),('4681000000','강진군'),('4682000000','해남군'),('4683000000','영암군'),('4684000000','무안군'),('4686000000','함평군'),('4687000000','영광군'),('4688000000','장성군'),('4689000000','완도군'),('4690000000','진도군'),('4691000000','신안군'),('4711000000','포항시'),('4711100000','포항시 남구'),('4711300000','포항시 북구'),('4713000000','경주시'),('4715000000','김천시'),('4717000000','안동시'),('4719000000','구미시'),('4721000000','영주시'),('4723000000','영천시'),('4725000000','상주시'),('4728000000','문경시'),('4729000000','경산시'),('4772000000','군위군'),('4773000000','의성군'),('4775000000','청송군'),('4776000000','영양군'),('4777000000','영덕군'),('4782000000','청도군'),('4783000000','고령군'),('4784000000','성주군'),('4785000000','칠곡군'),('4790000000','예천군'),('4792000000','봉화군'),('4793000000','울진군'),('4794000000','울릉군'),('4812000000','창원시'),('4812100000','창원시 의창구'),('4812300000','창원시 성산구'),('4812500000','창원시 마산합포구'),('4812700000','창원시 마산회원구'),('4812900000','창원시 진해구'),('4817000000','진주시'),('4822000000','통영시'),('4824000000','사천시'),('4825000000','김해시'),('4827000000','밀양시'),('4831000000','거제시'),('4833000000','양산시'),('4872000000','의령군'),('4873000000','함안군'),('4874000000','창녕군'),('4882000000','고성군'),('4884000000','남해군'),('4885000000','하동군'),('4886000000','산청군'),('4887000000','함양군'),('4888000000','거창군'),('4889000000','합천군'),('5011000000','제주시'),('5013000000','서귀포시');
/*!40000 ALTER TABLE `guguncodes` ENABLE KEYS */;
UNLOCK TABLES;

LOCK TABLES `sidocodes` WRITE;
/*!40000 ALTER TABLE `sidocodes` DISABLE KEYS */;
INSERT INTO `sidocodes` VALUES ('4200000000','강원도'),('4100000000','경기도'),('4800000000','경상남도'),('4700000000','경상북도'),('2900000000','광주광역시'),('2700000000','대구광역시'),('3000000000','대전광역시'),('2600000000','부산광역시'),('1100000000','서울특별시'),('3611000000','세종특별자치시'),('3100000000','울산광역시'),('2800000000','인천광역시'),('4600000000','전라남도'),('4500000000','전라북도'),('5000000000','제주특별자치도'),('4400000000','충청남도'),('4300000000','충청북도');
/*!40000 ALTER TABLE `sidocodes` ENABLE KEYS */;
UNLOCK TABLES;

INSERT INTO interest_apt (user_id, apt_seq) VALUES
                                                ('ssafy', '11110-100'),
                                                ('ssafy', '11110-2445'),
                                                ('ssafy', '11140-1217'),
                                                ('ssafy', '11680-5114'),
                                                ('ssafy', '48123-156');

INSERT INTO board (user_id, subject, content, hit, register_time)
VALUES
    ('boss', '아파트 층수에 따른 장단점', '아파트 층수에 따라 생활의 질이 어떻게 달라지나요? 경험 공유 부탁드립니다.', 45, NOW()),
    ('ssafy', '부동산 매매 시 공인중개사 수수료', '공인중개사 수수료가 어떻게 계산되는지 궁금합니다.', 38, NOW()),
    ('snoopy', '재개발 지역 투자 유망성', '재개발 지역에 투자하려는데, 어떤 점을 주의해야 하나요?', 60, NOW()),
    ('dragon', '원룸 임대 계약 시 확인 사항', '원룸 계약 시 반드시 확인해야 할 서류가 있을까요?', 33, NOW()),
    ('ssafy', '상가 매매 후 관리비 문제', '상가를 매수한 후 관리비 문제가 생길 수 있다는데, 어떻게 해결할까요?', 22, NOW()),
    ('boss', '오피스텔 투자 시 수익률 계산법', '오피스텔 투자 시 기대 수익률은 어떻게 계산하나요?', 55, NOW());


INSERT INTO comments (article_no, user_id, content, register_time)
VALUES
    -- 아파트 층수에 따른 장단점
    (1, 'snoopy', '저층은 출입이 편리하고, 고층은 조망권이 좋아요. 필요에 따라 선택하세요.', NOW()),
    (1, 'ssafy', '고층은 여름에 더 시원하지만, 엘리베이터 문제도 고려해야 해요.', NOW()),

    -- 부동산 매매 시 공인중개사 수수료
    (2, 'dragon', '수수료는 거래 금액에 따라 정해집니다. 계약 전에 협의 가능해요.', NOW()),
    (2, 'boss', '법정 수수료율을 넘는 요구를 받으면 신고할 수 있어요.', NOW()),

    -- 재개발 지역 투자 유망성
    (3, 'boss', '재개발 지역은 사업 진행 상태를 반드시 확인하세요. 리스크가 있을 수 있어요.', NOW()),
    (3, 'ssafy', '주변 인프라와 공시지가 상승 가능성도 중요합니다.', NOW()),

    -- 원룸 임대 계약 시 확인 사항
    (4, 'snoopy', '임대차 계약서와 등기부등본을 꼭 확인하세요.', NOW()),
    (4, 'dragon', '임대인의 신분증과 부동산 매매 이력을 확인하면 좋아요.', NOW()),

    -- 상가 매매 후 관리비 문제
    (5, 'boss', '상가 관리비 문제는 관리사무소와 계약서를 다시 검토해야 해요.', NOW()),
    (5, 'snoopy', '관리비 항목별로 구체적으로 나와 있는지 확인하세요.', NOW()),

    -- 오피스텔 투자 시 수익률 계산법
    (6, 'ssafy', '월세 수익과 관리비, 공실 위험을 함께 고려하세요.', NOW()),
    (6, 'dragon', '세금까지 포함한 총 투자 비용을 계산해보세요.', NOW());



INSERT IGNORE INTO latest_housedeals (
    apt_seq,
    apt_dong,
    floor,
    deal_year,
    deal_month,
    deal_day,
    exclu_use_ar,
    deal_amount,
    deal_count
)
SELECT
    hd.apt_seq,
    hd.apt_dong,
    hd.floor,
    hd.deal_year,
    hd.deal_month,
    hd.deal_day,
    hd.exclu_use_ar,
    hd.deal_amount,
    counts.total_deals
FROM housedeals hd
         JOIN (
    SELECT apt_seq,
           MAX(CONCAT(deal_year, LPAD(deal_month, 2, '0'), LPAD(deal_day, 2, '0'))) AS latest_date
    FROM housedeals
    GROUP BY apt_seq
) latest ON hd.apt_seq = latest.apt_seq
    AND CONCAT(hd.deal_year, LPAD(hd.deal_month, 2, '0'), LPAD(hd.deal_day, 2, '0')) = latest.latest_date
         JOIN (
    SELECT apt_seq, COUNT(*) as total_deals
    FROM housedeals
    GROUP BY apt_seq
) counts ON hd.apt_seq = counts.apt_seq;


INSERT INTO house_price_stats (
    apt_seq,
    apt_nm,
    latest_deal_amount,
    prev_deal_amount,
    price_change_rate,
    deal_count
)
WITH ranked_deals AS (
    SELECT
        hd.apt_seq,
        hd.deal_amount,
        ROW_NUMBER() OVER (PARTITION BY hd.apt_seq ORDER BY deal_year DESC, deal_month DESC, deal_day DESC) as rn
    FROM housedeals hd
)
SELECT
    r1.apt_seq,
    hi.apt_nm,
    r1.deal_amount as latest_deal_amount,
    r2.deal_amount as prev_deal_amount,
    CASE
        WHEN r2.deal_amount IS NOT NULL
            THEN ROUND(
                (CAST(REPLACE(r1.deal_amount, ',', '') AS DECIMAL(10,0)) -
                 CAST(REPLACE(r2.deal_amount, ',', '') AS DECIMAL(10,0))) /
                CAST(REPLACE(r2.deal_amount, ',', '') AS DECIMAL(10,0)) * 100,
                1)
        ELSE 0
        END AS price_change_rate,
    COUNT(*) OVER (PARTITION BY r1.apt_seq) as deal_count
FROM ranked_deals r1
         JOIN houseinfos hi ON r1.apt_seq = hi.apt_seq
         LEFT JOIN ranked_deals r2 ON r1.apt_seq = r2.apt_seq AND r2.rn = 2
WHERE r1.rn = 1;