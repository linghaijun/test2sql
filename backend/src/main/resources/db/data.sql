-- Text2SQL Test Data
-- 插入测试数据

-- 插入部门数据（10个部门）
INSERT INTO departments (name, budget, location) VALUES
('技术部', 5000000.00, '北京'),
('市场部', 3000000.00, '上海'),
('销售部', 4000000.00, '广州'),
('人事部', 2000000.00, '北京'),
('财务部', 2500000.00, '深圳'),
('产品部', 3500000.00, '杭州'),
('运营部', 2800000.00, '成都'),
('客服部', 1500000.00, '武汉'),
('研发部', 6000000.00, '北京'),
('设计部', 2200000.00, '上海');

-- 插入员工数据（50个员工）
INSERT INTO employees (name, age, department_id, salary, hire_date, email, phone, position, status) VALUES
('张伟', 28, 1, 15000.00, '2021-03-15', 'zhangwei@company.com', '13800138001', '高级工程师', 'active'),
('王芳', 32, 1, 18000.00, '2019-06-20', 'wangfang@company.com', '13800138002', '技术主管', 'active'),
('李娜', 26, 2, 12000.00, '2022-01-10', 'lina@company.com', '13800138003', '市场专员', 'active'),
('刘强', 35, 3, 20000.00, '2018-04-05', 'liuqiang@company.com', '13800138004', '销售经理', 'active'),
('陈静', 29, 4, 14000.00, '2020-09-18', 'chenjing@company.com', '13800138005', '人事专员', 'active'),
('杨洋', 31, 5, 16000.00, '2019-11-22', 'yangyang@company.com', '13800138006', '财务主管', 'active'),
('赵敏', 27, 6, 13000.00, '2021-07-08', 'zhaomin@company.com', '13800138007', '产品经理', 'active'),
('黄磊', 33, 7, 15000.00, '2018-12-01', 'huanglei@company.com', '13800138008', '运营主管', 'active'),
('周杰', 25, 8, 10000.00, '2022-05-15', 'zhoujie@company.com', '13800138009', '客服专员', 'active'),
('吴涛', 30, 9, 22000.00, '2017-08-20', 'wutao@company.com', '13800138010', '研发经理', 'active'),
('孙丽', 24, 1, 11000.00, '2023-02-01', 'sunli@company.com', '13800138011', '初级工程师', 'active'),
('朱明', 36, 3, 25000.00, '2016-03-12', 'zhuming@company.com', '13800138012', '销售总监', 'active'),
('马超', 28, 2, 14000.00, '2020-11-05', 'machao@company.com', '13800138013', '市场主管', 'active'),
('胡婷', 26, 4, 12000.00, '2021-09-10', 'huting@company.com', '13800138014', '招聘专员', 'active'),
('郭浩', 34, 9, 19000.00, '2018-01-25', 'guohao@company.com', '13800138015', '架构师', 'active'),
('林峰', 29, 6, 16000.00, '2019-10-18', 'linfeng@company.com', '13800138016', '高级产品经理', 'active'),
('何雪', 27, 5, 13000.00, '2020-07-22', 'hexue@company.com', '13800138017', '会计', 'active'),
('高勇', 31, 7, 14500.00, '2019-04-30', 'gaoyong@company.com', '13800138018', '运营专员', 'active'),
('罗琴', 25, 8, 9500.00, '2022-08-12', 'luoqin@company.com', '13800138019', '客服代表', 'active'),
('梁军', 32, 10, 17000.00, '2018-06-15', 'liangjun@company.com', '13800138020', '设计主管', 'active'),
('宋伟', 23, 1, 9000.00, '2023-06-01', 'songwei@company.com', '13800138021', '实习工程师', 'active'),
('唐芳', 28, 2, 13500.00, '2020-03-08', 'tangfang@company.com', '13800138022', '市场经理', 'active'),
('许刚', 30, 3, 16500.00, '2019-02-14', 'xugang@company.com', '13800138023', '销售主管', 'active'),
('韩梅', 26, 4, 11500.00, '2021-12-20', 'hanmei@company.com', '13800138024', '培训专员', 'active'),
('邓凯', 35, 9, 21000.00, '2017-05-10', 'dengkai@company.com', '13800138025', '技术总监', 'active'),
('冯霞', 29, 5, 14000.00, '2020-01-18', 'fengxia@company.com', '13800138026', '财务专员', 'active'),
('曹磊', 27, 6, 15500.00, '2020-10-25', 'caolei@company.com', '13800138027', '产品专员', 'active'),
('彭辉', 33, 7, 16000.00, '2018-09-12', 'penghui@company.com', '13800138028', '运营经理', 'active'),
('董燕', 24, 8, 8800.00, '2022-11-05', 'dongyan@company.com', '13800138029', '客服助理', 'active'),
('袁波', 31, 10, 18000.00, '2019-07-20', 'yuanbo@company.com', '13800138030', '高级设计师', 'active'),
('蒋红', 28, 1, 14500.00, '2020-05-15', 'jianghong@company.com', '13800138031', '中级工程师', 'active'),
('沈杰', 26, 3, 13000.00, '2021-04-08', 'shenjie@company.com', '13800138032', '销售代表', 'active'),
('韦丽', 30, 2, 15000.00, '2019-08-22', 'weili@company.com', '13800138033', '市场策划', 'active'),
('谢涛', 29, 9, 17500.00, '2019-03-15', 'xietao@company.com', '13800138034', '高级开发', 'active'),
('邹婷', 25, 6, 12500.00, '2021-10-10', 'zouting@company.com', '13800138035', '产品助理', 'active'),
('喻鹏', 32, 5, 15500.00, '2018-11-28', 'yupeng@company.com', '13800138036', '财务分析师', 'active'),
('柏晨', 27, 7, 13500.00, '2020-12-05', 'baichen@company.com', '13800138037', '运营专员', 'active'),
('窦琳', 26, 4, 11000.00, '2022-02-18', 'doulin@company.com', '13800138038', '行政专员', 'active'),
('葛强', 34, 3, 22000.00, '2017-09-10', 'geqiang@company.com', '13800138039', '大客户经理', 'active'),
('范敏', 28, 10, 16500.00, '2019-06-25', 'fanmin@company.com', '13800138040', 'UI设计师', 'active'),
('彭磊', 30, 1, 16000.00, '2019-05-12', 'penglei@company.com', '13800138041', '后端开发', 'active'),
('鲁涛', 29, 9, 18000.00, '2018-08-18', 'lutao@company.com', '13800138042', '前端开发', 'active'),
('岳霞', 25, 2, 12000.00, '2021-11-22', 'yuexia@company.com', '13800138043', '市场助理', 'active'),
('尹超', 31, 8, 10500.00, '2020-06-30', 'yinchao@company.com', '13800138044', '客服主管', 'active'),
('黎芳', 27, 6, 14000.00, '2020-08-15', 'lifang@company.com', '13800138045', '产品运营', 'active'),
('叶军', 33, 7, 15000.00, '2018-05-20', 'yejun@company.com', '13800138046', '内容运营', 'active'),
('钱伟', 24, 1, 10000.00, '2022-09-01', 'qianwei@company.com', '13800138047', '测试工程师', 'active'),
('洪磊', 28, 3, 14500.00, '2020-04-12', 'honglei@company.com', '13800138048', '销售专员', 'active'),
('崔婷', 26, 5, 12500.00, '2021-03-25', 'cuiting@company.com', '13800138049', '出纳', 'active'),
('姜浩', 30, 10, 17500.00, '2019-01-15', 'jianghao@company.com', '13800138050', '视觉设计师', 'active');

-- 更新部门经理ID
UPDATE departments SET manager_id = 2 WHERE id = 1;
UPDATE departments SET manager_id = 12 WHERE id = 2;
UPDATE departments SET manager_id = 4 WHERE id = 3;
UPDATE departments SET manager_id = 5 WHERE id = 4;
UPDATE departments SET manager_id = 6 WHERE id = 5;
UPDATE departments SET manager_id = 7 WHERE id = 6;
UPDATE departments SET manager_id = 8 WHERE id = 7;
UPDATE departments SET manager_id = 18 WHERE id = 8;
UPDATE departments SET manager_id = 10 WHERE id = 9;
UPDATE departments SET manager_id = 20 WHERE id = 10;

-- 插入产品类别数据
INSERT INTO product_categories (name, description) VALUES
('电子产品', '各类电子设备和数码产品'),
('办公用品', '日常办公所需用品'),
('服装配饰', '服装、鞋帽及配饰'),
('家居用品', '家庭生活用品'),
('图书音像', '图书、音乐及影视作品'),
('运动户外', '运动器材及户外装备'),
('食品饮料', '食品及饮料产品'),
('美妆个护', '美容及个人护理用品');

-- 插入产品数据（30个产品）
INSERT INTO products (name, category_id, price, stock, description, status) VALUES
('笔记本电脑 Pro', 1, 8999.00, 150, '高性能商务笔记本电脑', 'active'),
('无线鼠标', 1, 199.00, 500, '蓝牙无线鼠标', 'active'),
('机械键盘', 1, 459.00, 300, '青轴机械键盘', 'active'),
('显示器 27寸', 1, 2199.00, 120, '2K高清显示器', 'active'),
('A4打印纸', 2, 49.90, 1000, '优质A4打印纸', 'active'),
('签字笔套装', 2, 29.90, 800, '12支装签字笔', 'active'),
('文件夹', 2, 15.90, 600, 'A4文件夹', 'active'),
('商务笔记本', 2, 35.00, 400, '优质记事本', 'active'),
('男士衬衫', 3, 259.00, 200, '商务长袖衬衫', 'active'),
('女士西装', 3, 599.00, 150, '职业女性西装', 'active'),
('休闲鞋', 3, 399.00, 180, '舒适休闲鞋', 'active'),
('皮带', 3, 159.00, 250, '真皮皮带', 'active'),
('床上四件套', 4, 399.00, 100, '纯棉床上用品', 'active'),
('台灯', 4, 129.00, 300, 'LED护眼台灯', 'active'),
('收纳盒套装', 4, 79.90, 400, '多功能收纳盒', 'active'),
('咖啡杯', 4, 49.90, 500, '保温咖啡杯', 'active'),
('Python编程', 5, 89.00, 200, 'Python入门教程', 'active'),
('数据结构', 5, 69.00, 180, '计算机科学经典', 'active'),
('轻音乐专辑', 5, 49.00, 150, '放松音乐合集', 'active'),
('跑步机', 6, 2999.00, 80, '家用折叠跑步机', 'active'),
('瑜伽垫', 6, 99.00, 350, '加厚防滑瑜伽垫', 'active'),
('篮球', 6, 199.00, 200, '标准比赛篮球', 'active'),
('进口咖啡豆', 7, 128.00, 300, '精品咖啡豆', 'active'),
('有机茶叶', 7, 88.00, 250, '高山有机茶', 'active'),
('坚果礼盒', 7, 168.00, 200, '混合坚果礼盒', 'active'),
('护肤套装', 8, 599.00, 120, '补水护肤套装', 'active'),
('口红套装', 8, 299.00, 180, '热门色号套装', 'active'),
('洗发水', 8, 79.00, 400, '滋润型洗发水', 'active'),
('牙膏套装', 8, 39.90, 600, '家庭装牙膏', 'active'),
('香水', 8, 459.00, 100, '淡香型香水', 'active');

-- 插入客户数据（25个客户）
INSERT INTO customers (name, email, phone, address, city, registration_date) VALUES
('李明', 'liming@email.com', '13900139001', '朝阳区建国路88号', '北京', '2022-01-15'),
('王丽', 'wangli@email.com', '13900139002', '浦东新区陆家嘴路100号', '上海', '2022-02-20'),
('张强', 'zhangqiang@email.com', '13900139003', '天河区天河路200号', '广州', '2022-03-10'),
('刘芳', 'liufang@email.com', '13900139004', '南山区科技园路50号', '深圳', '2022-04-05'),
('陈杰', 'chenjie@email.com', '13900139005', '西湖区文三路150号', '杭州', '2022-05-18'),
('杨雪', 'yangxue@email.com', '13900139006', '武侯区人民南路80号', '成都', '2022-06-22'),
('赵刚', 'zhaogang@email.com', '13900139007', '江汉区解放大道120号', '武汉', '2022-07-30'),
('黄敏', 'huangmin@email.com', '13900139008', '鼓楼区中山路60号', '南京', '2022-08-15'),
('周涛', 'zhoutao@email.com', '13900139009', '雁塔区高新路90号', '西安', '2022-09-08'),
('吴婷', 'wuting@email.com', '13900139010', '市南区香港路70号', '青岛', '2022-10-12'),
('孙磊', 'sunlei@email.com', '13900139011', '福田区深南大道100号', '深圳', '2022-11-20'),
('朱琴', 'zhuqin@email.com', '13900139012', '江北区观音桥路30号', '重庆', '2022-12-05'),
('马超', 'machao@email.com', '13900139013', '东湖区八一大道50号', '南昌', '2023-01-10'),
('胡霞', 'huxia@email.com', '13900139014', '历下区泉城路80号', '济南', '2023-02-15'),
('郭伟', 'guowei@email.com', '13900139015', '和平区南京路120号', '天津', '2023-03-20'),
('林红', 'linhong@email.com', '13900139016', '五华区东风路60号', '昆明', '2023-04-08'),
('何波', 'hebo@email.com', '13900139017', '青秀区民族大道100号', '南宁', '2023-05-12'),
('高燕', 'gaoyan@email.com', '13900139018', '雨花区韶山路40号', '长沙', '2023-06-18'),
('罗勇', 'luoyong@email.com', '13900139019', '金水区经三路70号', '郑州', '2023-07-25'),
('梁丽', 'liangli@email.com', '13900139020', '道里区中央大街90号', '哈尔滨', '2023-08-30'),
('宋刚', 'songgang@email.com', '13900139021', '沈河区青年大街110号', '沈阳', '2023-09-15'),
('唐敏', 'tangmin@email.com', '13900139022', '南关区人民大街50号', '长春', '2023-10-20'),
('许磊', 'xulei@email.com', '13900139023', '小店区长风街80号', '太原', '2023-11-08'),
('韩梅', 'hanmei@email.com', '13900139024', '桥西区中山路60号', '石家庄', '2023-12-15'),
('邓涛', 'dengtao@email.com', '13900139025', '城关区天水路100号', '兰州', '2024-01-10');

-- 插入订单数据（100个订单）
INSERT INTO orders (order_number, customer_id, product_id, quantity, unit_price, total_amount, order_date, status, shipping_address, notes)
SELECT 
    CONCAT('ORD', LPAD(seq, 6, '0')),
    FLOOR(1 + RAND() * 25),
    FLOOR(1 + RAND() * 30),
    FLOOR(1 + RAND() * 5),
    0,
    0,
    DATE_ADD('2023-01-01', INTERVAL FLOOR(RAND() * 365) DAY),
    ELT(1 + FLOOR(RAND() * 4), 'pending', 'processing', 'shipped', 'completed'),
    '',
    ''
FROM (
    SELECT @rownum := @rownum + 1 AS seq
    FROM information_schema.columns c1, information_schema.columns c2, (SELECT @rownum := 0) r
    LIMIT 100
) t;

-- 更新订单的单价和总金额
UPDATE orders o
JOIN products p ON o.product_id = p.id
SET 
    o.unit_price = p.price,
    o.total_amount = p.price * o.quantity;

-- 更新客户的总订单数和总消费金额
UPDATE customers c
SET 
    total_orders = (SELECT COUNT(*) FROM orders WHERE customer_id = c.id),
    total_amount = (SELECT COALESCE(SUM(total_amount), 0) FROM orders WHERE customer_id = c.id);
