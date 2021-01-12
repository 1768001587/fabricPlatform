# FabricPlatform说明文档

## 一.实体类及对应数据库说明

**1.one.hust.edu.cn.entities.CommonResult<T>**

```java
public class CommonResult<T> {//返回的前端的通用的json实体
    private Integer code;//返回状态码
    private String message;//返回消息
    private T data;//返回数据
    public CommonResult(Integer code,String message){
        this(code,message,null);
    }
}
```



**2.one.hust.edu.cn.entities.User**

```java
public class User implements Serializable {//用户实体类
    private Integer id;//用户id
    private String username;//用户名
    private String password;//密码
    private Integer fabricUserId;//fabric中的id
}
```

CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `user_name` varchar(30) NOT NULL COMMENT '用户账号',
  `password` varchar(100) DEFAULT '' COMMENT '密码',
  `fabric_user_id` varchar(50) NOT NULL COMMENT 'fabric 用户id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=119 DEFAULT CHARSET=utf8 COMMENT='用户信息表';



**3.one.hust.edu.cn.entities.DataSample**

```java
public class DataSample implements Serializable {
    private Integer id;
    private Integer channelId;
    private String data;
    private String dataName;
    private String dataType;
    private Double dataSize;
    private Integer originUserId;
    private Timestamp createdTime;
    private Timestamp modifiedTime;
}
```

CREATE TABLE `data_sample` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '数据id',
  `data` text COMMENT '模拟数据',
  `data_name` varchar(255) DEFAULT NULL COMMENT '数据名称',
  `data_type` varchar(255) DEFAULT NULL COMMENT '数据类型',
  `data_size` double DEFAULT NULL COMMENT '数据大小',
  `origin_user_id` int(11) DEFAULT NULL COMMENT '所属人id，对应user_id',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modified_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=170 DEFAULT CHARSET=utf8 COMMENT='模拟数据表';



**4.one.hust.edu.cn.entities.DataAuthority**

```java
public class DataAuthority implements Serializable {
    private Integer id;
    private Integer userId;
    private Integer dataSampleId;
    private Integer authorityKey;//用户权限 1代表查看文件 2代表修改文件 3代表删除文件
}
```

CREATE TABLE `channel_authority` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'channel权限id',
  `channel_id` bigint(11) NOT NULL COMMENT 'channelId',
  `user_id` bigint(11) NOT NULL COMMENT '用户id',
  `authority_key` bigint(11) NOT NULL COMMENT '权限字段 1代表添加权限',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;



**5.one.hust.edu.cn.entities.Channel**

```java
public class Channel implements Serializable {
    private Integer id;
    private String channelName;
}
```

CREATE TABLE `channel` (
  `id` bigint(11) NOT NULL COMMENT 'channelID',
  `channel_name` varchar(255) NOT NULL COMMENT 'channel名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



**6.one.hust.edu.cn.entities.ChannelAuthority**

```java
public class ChannelAuthority implements Serializable {
    private Integer id;
    private Integer channelId;
    private Integer userId;
    private Integer authorityKey;//权限字段 1代表添加权限
}

```

CREATE TABLE `channel_authority` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'channel权限id',
  `channel_id` bigint(11) NOT NULL COMMENT 'channelId',
  `user_id` bigint(11) NOT NULL COMMENT '用户id',
  `authority_key` bigint(11) NOT NULL COMMENT '权限字段 1代表添加权限',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;



**7.one.hust.edu.cn.vo.AllChannelUserVO**

```java
public class AllChannelUserVO {
    private Integer userId;
    private String userName;
    private Integer channelId;
    private String channelName;
    private Set<Integer> channelAuthoritySet;
}

```



**8.one.hust.edu.cn.vo.AllDataUserAuthorityVO**

```java
public class AllDataUserAuthorityVO {
    private Integer userId;
    private String userName;
    private String channelName;
    private Integer dataId;
    private String dataName;
    private Set<Integer> dataAuthoritySet;
}

```



**9.one.hust.edu.cn.vo.DataUserAuthorityVO**

```
public class DataUserAuthorityVO implements Serializable {
    private DataSample dataSample;
    private Set<Integer> authoritySet;
}

```



## 二.用户接口

接口路径：localhost:8080/user/login

接口参数：one.hust.edu.cn.entities.User

返回类型：one.hust.edu.cn.entities.CommonResult<T>

接口说明：用户登录接口，需要用户实体类的username和password，用户进行登录成功后，会产生一个token，有效时间为6000000毫秒。返回CommonResult数据为JSON数据，user对应登录成功的用户信息，token对应该用户产生的token值，该用户在进行后续操作时都应该携带该token，以此验证身份。

接口代码：

```java
//登录
    @PostMapping(value = "/user/login")
    @LoginToken
    public CommonResult login(@RequestBody User user){
        JSONObject jsonObject = new JSONObject();
        boolean result = userService.login(user);
        if(result) {
            User uresult = userService.findUserByUsername(user.getUsername());
            String token = JwtUtil.createJWT(Integer.MAX_VALUE, uresult);
            jsonObject.put("user", uresult);
            jsonObject.put("token", token);
            return new CommonResult<>(200,"登录成功",jsonObject);
        }
        else {
            return new CommonResult<>(400,"登录失败,用户不存在或用户名或密码错误",null);
        }
    }
```



接口路径：localhost:8080/user/register

接口参数：one.hust.edu.cn.entities.User

返回类型：one.hust.edu.cn.entities.CommonResult<T>

接口说明：用户注册接口，需要用户实体类的username和password，用户进行注册成功后，会产生一个token，有效时间为6000000毫秒。返回CommonResult数据为JSON数据，user对应登录成功的用户信息，token对应该用户产生的token值，该用户在进行后续操作时都应该携带该token，以此验证身份。

接口代码：

```java
//注册
    @PostMapping(value = "/user/register")
    @LoginToken
    public CommonResult register(@RequestBody User user){
            JSONObject jsonObject = new JSONObject();
            if(userService.findUserByUsername(user.getUsername())!=null) return new CommonResult<>(400,"注册失败,用户名已存在",null);
            boolean result = userService.register(user);
            if(result) {
                String token = JwtUtil.createJWT(Integer.MAX_VALUE, user);
                jsonObject.put("token", token);
                jsonObject.put("user", user);
                return new CommonResult<>(200,"注册成功",jsonObject);
            }
            else return new CommonResult<>(400,"注册失败,请联系系统管理员",null);
    }
```



## 三.文件接口

接口路径：localhost:8080/data/uploadFile/{channelId}

接口参数：MultipartFile file

返回类型：one.hust.edu.cn.entities.CommonResult<T>

接口说明：上传文件接口，前端需要传输MultipartFile类型，参数名为file的文件，以及选择上传的通道Id，并且在头部带上该用户的token。前端传输的文件将保存至本机的**D:/研究生资料/南六218实验室/代炜琦项目组文件/github同步代码/uploadFilePackage/**文件夹下，并且保存在数据库中，返回CommonResult数据为MyFile实体类。

接口代码：

```java
//上传文件
    @CheckToken
    @PostMapping(value = "/data/uploadFile/{channelId}")
    @ResponseBody
    @Transactional(rollbackFor=Exception.class) // Transactional注解默认在抛出uncheck异常（继承自Runtime Exception或 Error ）时才会回滚 而IO SQL等异常属于check异常，所以不会回滚
    public CommonResult uploadFile(@RequestParam("file") MultipartFile file, @PathVariable("channelId") Integer channelId, HttpServletRequest httpServletRequest){
        //获取文件名
        String fileName = file.getOriginalFilename();
        String filePath = "D:/研究生资料/南六218实验室/代炜琦项目组文件/github同步代码/uploadFilePackage/";
        // 从 http 请求头中取出 token
        String token = httpServletRequest.getHeader("token");
        Integer originUserId = JWT.decode(token).getClaim("id").asInt();

        try {
            //进行文件传输
            file.transferTo(new File(filePath + fileName));
            DataSample dataSample = new DataSample();
            dataSample.setChannelId(channelId);//这里后面要做出选择channel
            dataSample.setDataName(filePath + fileName);
            //文件大小以KB作为单位
            // 首先先将.getSize()获取的Long转为String 单位为B
            Double size = Double.parseDouble(String.valueOf(file.getSize()));
            BigDecimal b = new BigDecimal(size);
            // 2表示2位 ROUND_HALF_UP表明四舍五入，
            size = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            // 此时size就是保留两位小数的浮点数
            dataSample.setDataSize(size);
            dataSample.setData(TxtUtil.getTxtContent(dataSample));
            dataSample.setOriginUserId(originUserId);
            dataSample.setDataType(fileName.substring(fileName.lastIndexOf(".")) + "文件");
            //初次创建时将初始时间和修改时间写成一样
            dataSample.setCreatedTime(new Timestamp(new Date().getTime()));
            dataSample.setModifiedTime(new Timestamp(new Date().getTime()));
            dataService.uploadFile(dataSample);
            log.info("************fabric上传文件操作记录区块链开始*****************");
            String result = "";
            // 1. 权限申请 一次上链
            String username = "userA";
            String dstChannelName = "channel1";
            String txId = fabricService.applyForCreateFile(username, dstChannelName, dataSample.getId()+"");
            System.out.println("1.创建文件成功 txId: " + txId);
            result+="1.创建文件成功 txId: " + txId+"\r\n";
            //hash
            // 3. 更新链上hash 二次上链
            String rawRes = fabricService.updateForCreateFile(TxtUtil.getTxtContent(dataSample), username, dstChannelName, dataSample.getId()+"", txId);
            System.out.println("2. 更新链上hash ： " + rawRes);
            result+="2. 更新链上hash ： " + rawRes+"\r\n";
            // 4. 授予用户文件的查改权限
            rawRes = fabricService.grantUserPermissionOnFile("channel1", dataSample.getId()+"", "read", "role1", Collections.singletonList(username));
            System.out.println("3.授予用户文件读取权限：" + rawRes);
            result+="3.授予用户文件读取权限：" + rawRes+"\r\n";
            rawRes = fabricService.grantUserPermissionOnFile("channel1", dataSample.getId()+"", "modify", "role1", Collections.singletonList(username));
            System.out.println("4.授予用户文件修改权限：" + rawRes);
            result+="4.授予用户文件修改权限：" + rawRes+"\r\n";
            //写入上传者权限
            dataAuthorityService.addMasterDataAuthority(originUserId, dataSample.getId());
            log.info("************fabric上传文件操作记录区块链结束*****************");
            return new CommonResult<>(200,"上传成功，文件位于："+filePath+fileName+"\r\n"+result, dataSample);
        } catch (Exception e) {
            return new CommonResult<>(400, e.getMessage(), null);
        }
    }

```



接口路径：localhost:8080/data/getDataList

接口参数：无

返回类型：one.hust.edu.cn.entities.CommonResult<T>

接口说明：获取所有文件接口，返回MyFile的List集合。

接口代码：

```java
//获取文件列表
    @CheckToken
    @GetMapping(value = "/data/getDataList")
    public CommonResult getDataList(HttpServletRequest httpServletRequest){
        // 从 http 请求头中取出 token
        String token = httpServletRequest.getHeader("token");
        Integer userId = JWT.decode(token).getClaim("id").asInt();
        List<DataAuthority> list = dataAuthorityService.findDataAuthorityByUserId(userId);//获取该用户的所有权限
        List<DataUserAuthorityVO> result = new ArrayList<>();
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < list.size(); i++) {
            DataUserAuthorityVO temp = new DataUserAuthorityVO();
            Integer dataSampleId = list.get(i).getDataSampleId();
            if(!set.contains(dataSampleId)){
                set.add(dataSampleId);
                temp.setDataSample(dataService.findDataById(dataSampleId));
                Set<Integer> s = new HashSet<>();
                for (int j = 0; j < list.size(); j++) {
                    if(list.get(j).getDataSampleId().equals(dataSampleId)){
                        s.add(list.get(j).getAuthorityKey());
                    }
                }
                temp.setAuthoritySet(s);
                result.add(temp);
            }
        }
        return new CommonResult<>(200,"获取该用户所有文件权限列表成功",result);
    }//获取文件列表
@GetMapping(value = "/getDataList")
public CommonResult getDataList(){
    List<MyFile> list = fileService.getDataList();
    return new CommonResult<>(200,"获取所有文件列表成功",list);
}
```



接口路径：localhost:8080/data/deleteDataById

接口参数：Map<String, String> params和HttpServletRequest httpServletRequest

返回类型：one.hust.edu.cn.entities.CommonResult<T>

接口说明：根据id删除文件接口，此接口需要由登录token，并且在头部从httpServletRequest中获取token，params参数中应该传dataId参数，代表待删除的文件id，成功删除返回200状态码。

接口代码：

```java
//根据文件id删除文件
    //上传文件
    @CheckToken
    @PostMapping(value = "/data/deleteDataById")
    @ResponseBody
    public CommonResult deleteDataById(@RequestBody Map<String, String> params, HttpServletRequest httpServletRequest) {
        Integer dataId = Integer.valueOf(params.get("dataId"));
        // 从 http 请求头中取出 token
        String token = httpServletRequest.getHeader("token");
        Integer result = dataService.deleteDataById(dataId);
        if (result < 1) {
            return new CommonResult<>(400, "不存在id为：" + dataId + "的文件", null);
        }
        return new CommonResult<>(200, "成功删除id为：" + dataId + "的文件,token为：" + token, null);
    }
```



接口路径：localhost:8080/data/getData

接口参数：Map<String, String> params

返回类型：one.hust.edu.cn.entities.CommonResult<T>

接口说明：根据id查看文件接口，此接口需要由登录token，并且在头部从httpServletRequest中获取token，params参数中应该传dataId参数，代表待查看的文件id。如果文件存在，以String类型返回文件内容，作为CommonResult中的数据返回。

接口代码：

```java
 //根据文件id获取文件内容
    @CheckToken
    @PostMapping(value = "/data/getData")
    @ResponseBody
    public CommonResult getData(@RequestBody Map<String, String> params, HttpServletRequest httpServletRequest) {
        Integer dataId = Integer.valueOf(params.get("dataId"));
        DataSample result = dataService.findDataById(dataId);
        if(result==null){
            return new CommonResult<>(400,"不存在id为："+dataId+"的文件",null);
        }
        log.info("************fabric读取文件操作记录区块链开始*****************");
        // 1. 申请读取权限
        String username = "userA";
        String dstChannelName = "channel1";
        String txId = fabricService.applyForReadFile(username, dstChannelName, String.valueOf(dataId));
        if (txId == null || txId.isEmpty()) {
            log.info("申请文件读取权限失败");
            return new CommonResult<>(300,"申请文件读取权限失败",null);
        }
        // 2. 读取文件
        // 从 http 请求头中取出 token
        String token = httpServletRequest.getHeader("token");
        String txtContent = TxtUtil.getTxtContent(result);

        // 3. 二次上链
        String rawRes = fabricService.updateForCreateFile(txtContent, username, dstChannelName, String.valueOf(dataId), txId);
        log.info("2. 二次上链 ： " + rawRes);
        log.info("************fabric读取文件操作记录区块链结束*****************");
        return new CommonResult<>(200, "文件token为：" + token + "\r\ntxId：" + txId, txtContent);

    }
```



接口路径：localhost:8080/data/updateData

接口参数：Map<String, String> params和HttpServletRequest httpServletRequest

返回类型：one.hust.edu.cn.entities.CommonResult<T>

接口说明：根据id查看文件接口，此接口需要由登录token，并且在头部从httpServletRequest中获取token，params参数中应该传dataId参数，代表待修改的文件id，还应该传String类型的dataContent参数，代表修改后的文件内容。修改文件过程为：首先根据文件id查找文件，获取查找到的文件路径，并且删除本地存储的该文件，创建一个新的文件，并把dataContent作为文件内容写入新文件；再更新数据库，更新数据库中data_sample表对应该文件的data为dataContent，data_size为修改后文件的大小和modified_time为修改的时间。

接口代码：

```java
//根据文件id对文件内容进行更新
    @CheckToken
    @PostMapping(value = "/data/updateData")
    @ResponseBody
    public CommonResult updateData(@RequestBody Map<String, String> params){
        Integer dataId = Integer.valueOf(params.get("dataId"));
        String dataContent = params.get("dataContent");
        DataSample dataSample = dataService.findDataById(dataId);
        if (dataSample == null) {
            return new CommonResult<>(400, "不存在id为：" + dataId + "的文件", null);
        }
        File old_file = new File(dataSample.getDataName());
        log.info("************fabric更新文件操作记录区块链开始*****************");
        // 1. 申请文件修改权限
        String username = "userA";
        String dstChannelName = "channel1";
        String txId = fabricService.applyForModifyFile(username, dstChannelName, String.valueOf(dataId));
        if (txId == null || txId.isEmpty()) {
            System.out.println("申请文件修改权限失败");
            return new CommonResult<>(300,"申请文件修改权限失败",null);
        }
        // 2. 修改文件
        old_file.delete();
        File new_file = new File(dataSample.getDataName());
        //创建新文件
        try {
            /* 写入Txt文件 */
            new_file.createNewFile(); // 创建新文件
            BufferedWriter out = new BufferedWriter(new FileWriter(new_file));
            out.write(dataContent); // \r\n即为换行
            out.flush(); // 把缓存区内容压入文件
            out.close(); // 最后记得关闭文件
        } catch (Exception e) {
            e.printStackTrace();
        }
        //更新数据库
        dataSample.setData(dataContent);
        Double size = Double.parseDouble(String.valueOf(new_file.length())) / 1024;
        BigDecimal b = new BigDecimal(size);
        size = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        dataSample.setDataSize(size);
        dataSample.setModifiedTime(new Timestamp(new Date().getTime()));
        dataService.updateFile(dataSample);

        // 3. 更新hash值到fabric 二次上链
        String res = fabricService.updateForModifyFile(TxtUtil.getTxtContent(dataSample), username, dstChannelName, String.valueOf(dataId), txId);
        log.info("更新hash值结果：" + res);
        log.info("************fabric更新文件操作记录区块链结束*****************");
        return new CommonResult<>(200, "id为：" + dataId + "的文件更新成功\r\ntxId："+txId, null);
    }
```



接口路径：localhost:8080/data/getDataListByOriginUserId

接口参数：HttpServletRequest httpServletRequest

返回类型：one.hust.edu.cn.entities.CommonResult<T>

接口说明：根据当前用户获取由他上传的文件列表，需要在头部加上当前用户的token信息。

接口代码：

```java
//根据上传者id获取文件列表
@CheckToken
@GetMapping(value = "/data/getDataListByOriginUserId")
public CommonResult getDataListByOriginUserId(HttpServletRequest httpServletRequest){
    // 从 http 请求头中取出 token
    String token = httpServletRequest.getHeader("token");
    Integer userId = JWT.decode(token).getClaim("id").asInt();
    List<DataSample> list = dataService.getDataListByOriginUserId(userId);
    return new CommonResult<>(200,"获取该用户所有文件列表成功",list);
}
```



## 四.文件权限接口

接口路径：localhost:8080/dataAuthority/addDataAuthority

接口参数：DataAuthority dataAuthority

返回类型：one.hust.edu.cn.entities.CommonResult<T>

接口说明：给指定User添加指定文件权限，给出的authorityKey必须为这几个数字：1：查看文件  2：修改文件  3：删除文件

接口代码：

```java
//给用户，文件添加权限
@Transactional
@PostMapping(value = "/dataAuthority/addDataAuthority")
public CommonResult addDataAuthority(@RequestBody DataAuthority dataAuthority) {
    Integer userId = dataAuthority.getUserId();
    Integer dataSampleId = dataAuthority.getDataSampleId();
    Integer authorityKey = dataAuthority.getAuthorityKey();
    User user = userService.findUserById(userId);
    DataSample dataSample = dataService.findDataById(dataSampleId);
    if(user==null) return new CommonResult<>(400,"添加权限失败，不存在userId为："+userId+"的用户",null);
    if(dataSample ==null) return new CommonResult<>(400,"添加权限失败，不存在dataSampleId为："+dataSampleId+"的文件",null);
    if(authorityKey!=1&&authorityKey!=2&&authorityKey!=3) return new CommonResult<>(400,"authorityKey请选择：" +
            "1：查看文件  2：修改文件  3：删除文件",null);
    log.info("************fabric添加文件权限操作记录写入区块链开始*****************");
    if(!grantPermissionService.grantUserPermissionOnFile(dataAuthority)){
        return new CommonResult<>(400,"fabric: 添加权限失败");
    }
    dataAuthorityService.addDataAuthority(dataAuthority);
    log.info("************fabric添加文件权限操作记录写入区块链结束*****************");
    return new CommonResult<>(200, "dataAuthority添加权限成功", dataAuthority);
}
```



接口路径：localhost:8080/dataAuthority/deleteDataAuthority

接口参数：DataAuthority dataAuthority

返回类型：one.hust.edu.cn.entities.CommonResult<T>

接口说明：撤销指定User对指定文件的权限

接口代码：

```java
//给用户，文件撤销权限
@Transactional
@PostMapping(value = "/dataAuthority/deleteDataAuthority")
public CommonResult deleteDataAuthority(@RequestBody DataAuthority dataAuthority) {
    Integer userId = dataAuthority.getUserId();
    Integer dataSampleId = dataAuthority.getDataSampleId();
    Integer authorityKey = dataAuthority.getAuthorityKey();
    User user = userService.findUserById(userId);
    DataSample dataSample = dataService.findDataById(dataSampleId);
    if(user==null) return new CommonResult<>(400,"添加权限失败，不存在userId为："+userId+"的用户",null);
    if(dataSample ==null) return new CommonResult<>(400,"添加权限失败，不存在dataSampleId为："+dataSampleId+"的文件",null);
    if(authorityKey!=1&&authorityKey!=2&&authorityKey!=3) return new CommonResult<>(400,"authorityKey请选择：" +
            "1：查看文件  2：修改文件  3：删除文件",null);
    log.info("************fabric撤销文件权限操作记录区块链开始*****************");
    if(!grantPermissionService.revokeUserPermissionOnFile(dataAuthority)){
        return new CommonResult<>(400,"fabric: 撤销权限失败");
    }
    Integer count = dataAuthorityService.deleteDataAuthority(dataAuthority);
    log.info("************fabric撤销文件权限操作记录区块链结束*****************");
    if(count>=1) return new CommonResult<>(200,"dataAuthority撤销权限成功",dataAuthority);
    else return new CommonResult<>(400,"撤销权限失败，请联系系统管理员",dataAuthority);
}
```



接口路径：localhost:8080/dataAuthority/getAllAuthority

接口参数：DataAuthority dataAuthority

返回类型：one.hust.edu.cn.entities.CommonResult<T>

接口说明：有后台管理员调用，返回所有用户及其拥有的不同文件的不同权限

接口代码：

```java
//获取所有权限，返回AllDataUserAuthorityVO类
@GetMapping(value = "/dataAuthority/getAllAuthority")
public CommonResult getAllAuthority() {
    List<AllDataUserAuthorityVO> result = new ArrayList<>();
    Set<List<Integer>> set = new LinkedHashSet<>();//保存用户Id与对应的文件id
    List<User> users = userService.getAllUser();
    List<DataSample> dataSamples = dataService.getDataList();
    for (int i = 0; i < users.size(); i++) {
        for (int j = 0; j < dataSamples.size(); j++) {
            List<Integer> s = new ArrayList<>();
            s.add(users.get(i).getId());
            s.add(dataSamples.get(j).getId());
            set.add(s);
        }
    }
    Iterator<List<Integer>> it = set.iterator();
    while (it.hasNext()) {
        List<Integer> tmp = it.next();
        List<DataAuthority> dataAuthorities = dataAuthorityService.findDataAuthorityByUserIdAndDataId(tmp.get(0),tmp.get(1));
        Set<Integer> authoritySet = new HashSet<>();
        for (int i = 0; i < dataAuthorities.size(); i++) {
            authoritySet.add(dataAuthorities.get(i).getAuthorityKey());
        }
        User user = userService.findUserById(tmp.get(0));
        DataSample dataSample = dataService.findDataById(tmp.get(1));
        AllDataUserAuthorityVO allDataUserAuthorityVO = new AllDataUserAuthorityVO();
        allDataUserAuthorityVO.setUserId(tmp.get(0));
        allDataUserAuthorityVO.setUserName(user.getUsername());
        allDataUserAuthorityVO.setChannelName(channelService.findChannelById(dataSample.getChannelId()).getChannelName());
        allDataUserAuthorityVO.setDataId(tmp.get(1));
        allDataUserAuthorityVO.setDataName(dataSample.getDataName());
        allDataUserAuthorityVO.setDataAuthoritySet(authoritySet);
        result.add(allDataUserAuthorityVO);
    }
    return new CommonResult<>(200,"查找成功",result);
}
```



接口路径：localhost:8080/dataAuthority/findDataAuthorityByUserId

接口参数：DataAuthority dataAuthority

返回类型：one.hust.edu.cn.entities.CommonResult<T>

接口说明：查找某个User所拥有的权限

接口代码：

```java
//查找某一用户的所有权限
@PostMapping(value = "/dataAuthority/findDataAuthorityByUserId")
public CommonResult findDataAuthorityByUserId(@RequestBody Map<String, String> params) {
    Integer userId = Integer.valueOf(params.get("userId"));
    User user = userService.findUserById(userId);
    if(user==null) return new CommonResult<>(400,"不存在userId为："+userId+"的用户",null);
    List<DataAuthority> result = dataAuthorityService.findDataAuthorityByUserId(userId);
    return new CommonResult<>(200,"查找成功",result);
}
```



接口路径：localhost:8080/dataAuthority/findDataAuthorityByDataId

接口参数：DataAuthority dataAuthority

返回类型：one.hust.edu.cn.entities.CommonResult<T>

接口说明：查找某个文件被拥有者的权限

接口代码：

```java
//查找某一文件的所有权限
@PostMapping(value = "/dataAuthority/findDataAuthorityByDataId")
public CommonResult findDataAuthorityByDataId(@RequestBody Map<String, String> params) {
    Integer dataSampleId = Integer.valueOf(params.get("dataSampleId"));
    DataSample dataSample = dataService.findDataById(dataSampleId);
    if(dataSample ==null) return new CommonResult<>(400,"不存在dataSampleId为："+dataSampleId+"的文件",null);
    List<DataAuthority> result = dataAuthorityService.findDataAuthorityByDataId(dataSampleId);
    return new CommonResult<>(200,"查找成功",result);
}
```



## 五.通道权限接口

接口路径：localhost:8080/channel/getAddAuthorityChannels

接口参数：HttpServletRequest httpServletRequest

返回类型：one.hust.edu.cn.entities.CommonResult<T>

接口说明：获取某用户可上传文件的通道

接口代码：

```java
@CheckToken
@GetMapping(value = "/channel/getAddAuthorityChannels")
public CommonResult getAddAuthorityChannels(HttpServletRequest httpServletRequest){
    // 从 http 请求头中取出 token
    String token = httpServletRequest.getHeader("token");
    Integer userId = JWT.decode(token).getClaim("id").asInt();
    List<String> list = channelAuthorityService.getAddAuthorityChannels(userId);
    List<Channel> result = new ArrayList<>();
    for (int i = 0; i < list.size(); i++) {
        Channel channel = channelService.findChannelByName(list.get(i));
        result.add(channel);
    }
    return new CommonResult<>(200,"获取所有该用户可上传文件channel成功",result);
}
```



接口路径：localhost:8080/channel/getAllAuthorityChannels

接口参数：

返回类型：one.hust.edu.cn.entities.CommonResult<T>

接口说明：获取所有通道及其拥有该通道上传文件权限的用户

接口代码：

```java
//获取所有权限，返回AllDataUserAuthorityVO类
@GetMapping(value = "/channel/getAllAuthorityChannels")
public CommonResult getAllAuthorityChannels() {
    List<User> users = userService.getAllUser();
    List<Channel> channels = channelService.getAllChannel();
    List<AllChannelUserVO> result = new ArrayList<>();
    Set<List<Integer>> set = new LinkedHashSet<>();//保存用户Id与对应的文件id
    for (int i = 0; i < users.size(); i++) {
        for (int j = 0; j < channels.size(); j++) {
            List<Integer> list = new ArrayList<>();
            list.add(users.get(i).getId());
            list.add(channels.get(j).getId());
            set.add(list);
        }
    }
    Iterator<List<Integer>> it = set.iterator();
    while (it.hasNext()) {
        List<Integer> tmp = it.next();
        List<ChannelAuthority> channelAuthorities = channelAuthorityService.findChannelAuthorityByUserIdAndChannelId(tmp.get(0),tmp.get(1));
        Set<Integer> authoritySet = new HashSet<>();
        for (int i = 0; i < channelAuthorities.size(); i++) {
            authoritySet.add(channelAuthorities.get(i).getAuthorityKey());
        }
        User user = userService.findUserById(tmp.get(0));
        Channel channel = channelService.findChannelById(tmp.get(1));
        AllChannelUserVO allChannelUserVO = new AllChannelUserVO();
        allChannelUserVO.setUserId(tmp.get(0));
        allChannelUserVO.setUserName(user.getUsername());
        allChannelUserVO.setChannelId(channel.getId());
        allChannelUserVO.setChannelName(channel.getChannelName());
        allChannelUserVO.setChannelAuthoritySet(authoritySet);
        result.add(allChannelUserVO);
    }
    return new CommonResult<>(200,"查找成功",result);
}
```



接口路径：localhost:8080/channel/addChannelAuthority

接口参数：ChannelAuthority channelAuthority

返回类型：one.hust.edu.cn.entities.CommonResult<T>

接口说明：给某个用户添加可以上传文件至某通道的权限

接口代码：

```java
//给用户添加管道权限
@Transactional
@PostMapping(value = "/channel/addChannelAuthority")
public CommonResult addDataAuthority(@RequestBody ChannelAuthority channelAuthority) {
    Integer channelId = channelAuthority.getChannelId();
    Integer userId = channelAuthority.getUserId();
    Channel channel = channelService.findChannelById(channelId);
    User user = userService.findUserById(userId);
    Integer authorityKey = channelAuthority.getAuthorityKey();
    List<ChannelAuthority> channelAuthoritys = channelAuthorityService.findChannelAuthority(channelAuthority);
    if(channelAuthoritys.size()>=1) return new CommonResult<>(400,"添加权限失败，该权限已存在",null);
    if(channel==null) return new CommonResult<>(400,"添加权限失败，不存在channelId为："+channelId+"的channel",null);
    if(user==null) return new CommonResult<>(400,"添加权限失败，不存在userId为："+userId+"的用户",null);
    if(authorityKey!=1) return new CommonResult<>(400,"authorityKey请选择：" +
            "1：在该channel上上传文件权限",null);
    log.info("************fabric添加管道权限操作记录区块链开始*****************");
    String fabricResult = fabricService.grantUserPermission2Add(channel.getChannelName(),"AAA",user.getUsername());
    log.info(fabricResult);
    channelAuthorityService.addChannelAuthority(channelAuthority);
    log.info("************fabric添加管道权限操作记录区块链结束*****************");
    return new CommonResult<>(200, "channelAuthority添加权限成功", channelAuthority);
}
```



接口路径：localhost:8080/channel/deleteChannelAuthority

接口参数：ChannelAuthority channelAuthority

返回类型：one.hust.edu.cn.entities.CommonResult<T>

接口说明：撤销某个用户可以上传文件至某通道的权限

接口代码：

```java
//给用户，文件添加权限
@Transactional
@PostMapping(value = "/channel/deleteChannelAuthority")
public CommonResult deleteChannelAuthority(@RequestBody ChannelAuthority channelAuthority) {
    Integer channelId = channelAuthority.getChannelId();
    Integer userId = channelAuthority.getUserId();
    Channel channel = channelService.findChannelById(channelId);
    User user = userService.findUserById(userId);
    Integer authorityKey = channelAuthority.getAuthorityKey();
    if(channel==null) return new CommonResult<>(400,"撤销权限失败，不存在channelId为："+channelId+"的channel",null);
    if(user==null) return new CommonResult<>(400,"撤销权限失败，不存在userId为："+userId+"的用户",null);
    if(authorityKey!=1) return new CommonResult<>(400,"authorityKey请选择：" +
            "1：在该channel上上传文件权限",null);

    Integer count = channelAuthorityService.deleteChannelAuthority(channelAuthority);

    if(count>=1) return new CommonResult<>(200, "channelAuthority撤销权限成功", channelAuthority);
    else return new CommonResult<>(200, "不存在该权限", channelAuthority);
}
```









后台已完成与fabric对接操作：

文件操作：

1.上传文件操作记录

2.读取文件操作记录

3.更新文件操作记录

4.溯源文件记录

文件权限操作：

1.添加文件权限操作记录

2.撤销文件权限操作记录

channel权限操作：

1.添加管道权限操作记录



还需与fabric进行接口对接操作：

用户操作：

1.对用户的增加记录上链

2.对用户的删除记录上链

文件操作：

1.删除文件记录上链

2.追踪文件记录



channel权限操作：

1.增加channel记录上链

2.删除channel记录上链

3.撤销channel权限记录上链





