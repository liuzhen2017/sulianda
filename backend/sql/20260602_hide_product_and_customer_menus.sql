UPDATE sys_menu
SET visible = '1'
WHERE component LIKE 'product/product%'
   OR component LIKE 'express/customer%';
