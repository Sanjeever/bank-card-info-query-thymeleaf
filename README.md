# Bank Card Info Query Thymeleaf

Issuing bank inquiry

This repository is the [Thymeleaf](https://www.thymeleaf.org/) implementation
of [bank-card-info-query](https://github.com/Sanjeever/bank-card-info-query)

## Production

Build the application for production:

```bash
docker build -t bank-card-info-query-thymeleaf .
docker run -d -p 8080:8080 --name bank-card-app bank-card-info-query-thymeleaf
```

## Thanks

- [cnbankcard](https://github.com/digglife/cnbankcard)
- [bootstrap](https://github.com/twbs/bootstrap)
- [RuoYi-Vue-Plus](https://github.com/dromara/RuoYi-Vue-Plus/blob/4.X/ruoyi-admin/src/main/resources/logback-plus.xml)

## LICENSE

[MIT](LICENSE)