# ThePermissions
```java 
/*********只需要在activity配置onRequestPermissionsResult*********/

@Override
public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    PermissionsManager.get().setPermissionsResult(requestCode,permissions,grantResults);
}

PermissionsManager.get().request(MainActivity.this,new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_CONTACTS}, new PermissionsCallback() {
    @Override
    public void granted() {
        //权限全部允许
    }
    @Override
    public void denied(String firstDenied) {
        //拒绝单个权限或者全部权限
    }
    @Override
    public void eachGranted(String permissions) {
        super.eachGranted(permissions);
        //每个被允许的权限
    }
    @Override
    public void eachDenied(String permissions) {
        super.eachDenied(permissions);
        //每个被拒绝的权限
    }
});


//单个权限String
PermissionsManager.get().request(this,String,PermissionsCallback);

//多个权限String[]
PermissionsManager.get().request(this,String[],PermissionsCallback);

//请求Manifest里面配置必须动态申请的所有权限
PermissionsManager.get().requestAll(this,PermissionsCallback);


```  

### 或者(or)(不需要在activity配置onRequestPermissionsResult)
```java 
MyPermissions.get(MainActivity.this).request(Manifest.permission.CAMERA, new PermissionsCallback() {
    @Override
    public void granted() {
        //权限全部允许
    }
    @Override
    public void denied(String firstDenied) {
        //拒绝单个权限或者全部权限
    }
   //此处忽略两个重写方法(eachGranted和eachDenied)
});

//请求Manifest里面配置必须动态申请的所有权限
MyPermissions.get(MainActivity.this).requestAll(PermissionsCallback);
```  
      
| 最新版本号 | [ ![Download](https://api.bintray.com/packages/zhongrui/mylibrary/MyPermissions/images/download.svg) ](https://bintray.com/zhongrui/mylibrary/MyPermissions/_latestVersion) |
|--------|----|
  
   
```gradle 
implementation 'com.github:MyPermissions:版本号看上面'
```
<br/>
<br/>
<br/>  

Thanks https://github.com/tbruyelle/RxPermissions
