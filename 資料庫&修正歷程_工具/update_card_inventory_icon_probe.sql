-- 背包圖示診斷：只測試「變身卡：狼人」(700502)。
-- 474 為目前客戶端已在使用的「傳送回家的卷軸」原生單格圖示。
-- 此檔不影響圖鑑 HTML 卡面、刺客首領卡或任何選取框素材。
UPDATE `etcitem`
SET `invgfx` = 474
WHERE `item_id` = 700502;

SELECT `item_id`, `name`, `invgfx`, `grdgfx`
FROM `etcitem`
WHERE `item_id` = 700502;
