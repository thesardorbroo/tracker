# Architecture

1. Entityni tekshiruvchi configni saqlab turadigan table/class kerak
Bu o'zida entityni qanaqb tekshirishni ushlab turadi. Misol uchun: 
Entity person, role based tekshiriladi, ROLE_ADMIN, ROLE_MODERATOR bo'lishi kerak.
yoki
Car entity, user id based tekshiriladi, admin user id verify qilsa bo'ldi

2. Entity tracker
Bu o'zida qaysi entity create/update/delete qilinvotganini ushlab turadi. Misol uchun
Person entity create qilinvoti, some user tarafidan, entity data

3. Verifications
Bu o'zida qaysi zapisga kim verify qiganini ushlab turadi. Misol uchun:
Person entity update zapsini, admin va moderator update verify qildi

## Cases
Entity ni ham oddiy user ham permissioni bor odam ham yasshi mumkun
