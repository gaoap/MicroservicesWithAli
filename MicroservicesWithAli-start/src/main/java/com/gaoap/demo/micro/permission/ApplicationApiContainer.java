package com.gaoap.demo.micro.permission;

import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 应用服务 API容器封装类
 */
@Data
public class ApplicationApiContainer {
    private final String serverKey;
    private final String serverName;
    /**
     * 公开接口集合
     */
    private List<ServerApiBO> publicApis = Collections.synchronizedList(new ArrayList<>());
    /**
     * 内部公开接口集合
     */
    private List<ServerApiBO> internalPublicApis = Collections.synchronizedList(new ArrayList<>());

    /**
     * 纳入权限控制的接口集合
     *
     * @param applicationContext
     */
    private List<ServerApiBO> accessApis = Collections.synchronizedList(new ArrayList<>());

    public ApplicationApiContainer(WebApplicationContext applicationContext, String serverKey, String serverName) {
        this.serverKey = serverKey;
        this.serverName = serverName;
        this.initThisApis(applicationContext);
    }

    private void initThisApis(WebApplicationContext applicationContext) {
        // 取出所有的Mapping
        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        // 获取url与类和方法的对应信息
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();
        // 遍历服务接口信息，筛选符合条件的数据
        map.forEach((mappingInfo, handlerMethod) -> {
            // 类
            Class<?> controllerClass = handlerMethod.getBeanType();
            // 包路径
            String classPackage = controllerClass.getName();
            if (verifyClassPackageHasProperties(classPackage, "com.gaoap.demo.micro.demos.**")) {
                // 方法
                Method method = handlerMethod.getMethod();
                System.out.println(controllerClass.getSimpleName());
                System.out.println(method.getName());
                // 获取方法请求类型
                String[] methodTypes=this.getMethodTypes(mappingInfo);

                System.out.println(mappingInfo.getPatternsCondition().getPatterns().size());
//
                // 获取方法路径
                String[] methodPaths = mappingInfo.getPatternsCondition().getPatterns().toArray(new String[]{});

                // 生成数据
                List<ServerApiBO> serverApiBOS = this.builderServerApiBO(
                        controllerClass.isAnnotationPresent(RequestMapping.class) ? controllerClass.getAnnotation(RequestMapping.class).value()[0] : controllerClass.getSimpleName(),
                        methodTypes,
                        methodPaths
                );
                System.out.println(controllerClass.getSimpleName());
                System.out.println(controllerClass.isAnnotationPresent(RequestMapping.class));
                // 查看类上是否包含@PublicApi注解
                if (controllerClass.isAnnotationPresent(PublicApi.class) || method.isAnnotationPresent(PublicApi.class)) {
                    PublicApi publicApi = controllerClass.isAnnotationPresent(PublicApi.class) ? controllerClass.getAnnotation(PublicApi.class) : method.getAnnotation(PublicApi.class);
                    if (publicApi.scope() == PublicScopeEnum.PUBLIC) {
                        this.publicApis.addAll(serverApiBOS);
                    } else {
                        this.internalPublicApis.addAll(serverApiBOS);
                    }
                } else {
                    this.accessApis.addAll(serverApiBOS);
                }
            }
        });
    }

    /**
     * 生成一个ServerApiBO对象
     */
    private List<ServerApiBO> builderServerApiBO(String moduleKey, String[] methodTypes, String[] methodPaths) {
        List<ServerApiBO> serverApiBOS = new ArrayList<>();
        if (methodTypes.length <= 0) {
            serverApiBOS.add(new ServerApiBO()
                    .setModuleKey(moduleKey)
                    .setModuleName(moduleKey)
                    .setApiType("POST")
                    .setApiPath(this.serverKey + methodPaths[0])
            );
            serverApiBOS.add(new ServerApiBO()
                    .setModuleKey(moduleKey)
                    .setModuleName(moduleKey)
                    .setApiType("PUT")
                    .setApiPath(this.serverKey + methodPaths[0])
            );
            serverApiBOS.add(new ServerApiBO()
                    .setModuleKey(moduleKey)
                    .setModuleName(moduleKey)
                    .setApiType("GET")
                    .setApiPath(this.serverKey + methodPaths[0])
            );
            serverApiBOS.add(new ServerApiBO()
                    .setModuleKey(moduleKey)
                    .setModuleName(moduleKey)
                    .setApiType("DELETE")
                    .setApiPath(this.serverKey + methodPaths[0])
            );
        } else {
            serverApiBOS.add(new ServerApiBO()
                    .setModuleKey(moduleKey)
                    .setModuleName(moduleKey)
                    .setApiType(methodTypes[0])
                    .setApiPath(this.serverKey + methodPaths[0])
            );
        }
        return serverApiBOS;
    }

    private String[] getMethodTypes(RequestMappingInfo mappingInfo){
        List<RequestMethod> requestMethodList = new ArrayList<>(mappingInfo.getMethodsCondition().getMethods());
        String[] methodTypes=new String[requestMethodList.size()];
        for (int i=0;i<requestMethodList.size();i++){
            methodTypes[i]=requestMethodList.get(i).toString();
        }
        return methodTypes;
    }
    /**
     * 验证包路径
     *
     * @param classPackage 需要验证的包路径
     * @param scanPackages 验证条件的包路径，可以传入多个
     * @return 验证结果，只要有一个条件符合，条件就会成立并返回True
     */
    private static boolean verifyClassPackageHasProperties(String classPackage, String... scanPackages) {
        for (String scanPackage : scanPackages) {
            System.out.println(buildRegexPackage(scanPackage));
            System.out.println(classPackage);
            System.out.println(Pattern.matches(buildRegexPackage(scanPackage), classPackage));
            if (Pattern.matches(buildRegexPackage(scanPackage), classPackage)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 转换验证条件，使其支持正则验证
     *
     * @param scanPackage 验证条件包路径
     * @return 验证条件正则
     */
    private static String buildRegexPackage(String scanPackage) {
        return scanPackage.replace("**", "[\\w]*") + ".[\\w]*";
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    class ServerApiBO {
        private String id;
        private String moduleKey;
        private String moduleName;
        private String apiName;
        private String apiPath;
        private String apiType;
    }
}


