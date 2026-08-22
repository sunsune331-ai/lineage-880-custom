-- 變身收藏圖鑑：卡片 700525 的顯示名稱校正。
-- 僅調整「變身卡」資料；怪物、掉落與既有 NPC 名稱維持原本的「安普長老」。

UPDATE `道具合成系統`
SET `note` = '變身卡：安普長者'
WHERE `item_id` = 700525;

UPDATE `etcitem`
SET `name` = '變身卡：安普長者',
    `name_id` = '變身卡：安普長者'
WHERE `item_id` = 700525;
