
/**
 * 	/**
 * 	 * 在之后的内容你可能会频繁的见到 “MergedBeanDefinition”这个词，因此这边先稍微讲解一下，有助于你更好的理解。
 * 	 MergedBeanDefinition：这个词其实不是一个官方词，但是很接近，该词主要是用来表示“合并的 bean 定义”，因为每次都写 “合并的 bean 定义” 有点太绕口，
 * 	    因此我在之后的注释或解析中或统一使用 MergedBeanDefinition来表示 “合并的 bean 定义”。
 *
 * 	 之所以称之为 “合并的”，是因为存在 “子定义”和 “父定义”的情况。对于一个bean定义来说，可能存在以下几种情况：
 * 		 (1)该BeanDefinition存在 “父定义”：首先使用 “父定义”的参数构建一个RootBeanDefinition，然后再使用该BeanDefinition的参数来进行覆盖。
 * 		 (2)该BeanDefinition不存在 “父定义”，并且该 BeanDefinition的类型是RootBeanDefinition：直接返回该RootBeanDefinition的一个克隆。
 * 		 (3)该BeanDefinition不存在 “父定义”，但是该 BeanDefinition的类型不是RootBeanDefinition：使用该 BeanDefinition的参数构建一个RootBeanDefinition。
 *
 * 	 之所以区分出2和3，是因为通常BeanDefinition在之前加载到BeanFactory 中的时候，通常是被封装成GenericBeanDefinition或 ScannedGenericBeanDefinition，
 * 	 	但是从这边之后bean的后续流程处理都是针对RootBeanDefinition，因此在这边会统一将BeanDefinition转换成RootBeanDefinition。
 *
 * 	 在我们日常使用的过程中，通常会是上面的第3种情况。如果我们使用 XML配置来注册bean，则该bean定义会被封装成：GenericBeanDefinition；
 * 	    如果我们使用注解的方式来注册bean，也就是<context:component-scan /> + @Compoment，则该bean定义会被封装成ScannedGenericBeanDefinition。

 * 
 * 
		已阅测试类 -> 已阅读类
 		CachedIntrospectionResultsTests-> todo

 		AbstractPropertyAccessorTests-> todo




		MutablePropertyValuesTests -> MutablePropertyValues
		AttributeAccessorSupportTests -> AttributeAccessorSupport
		BeanDefinitionTests -> RootBeanDefinition,
		SimpleAliasRegistryTests -> SimpleAliasRegistry
		DefaultSingletonBeanRegistryTests -> DefaultSingletonBeanRegistry
		AbstractBeanFactoryTests -> FactoryBeanRegistrySupport；
		ClassPathResourceTests -> ClassPathResource,封装了类加载,底层是classloader的getResourceAsStream和class的getResourceAsStream(String name)
									Matcher
									PropertiesPersisterTests -> PropertiesPersister 封装了Properties的各种方法，存取功能，Properties底层是一个Hashtable
		PropertiesBeanDefinitionReaderTests -> PropertiesBeanDefinitionReader 直接从properties文件或者Map里加载Bean，设置ConstructorArgumentValues,MutablePropertyValues,通过工厂的registerBeanDefinition实现注入

		ResolvableTypeTests


		bean生命周期可以参考LifecycleBean


		DefaultListableBeanFactoryTests ->  preInstantiateSingletons()父子定义的合并
											preInstantiateSingletons,(不是抽象类 && 是单例 && 不是懒加载)则实例化
											如果将一个继承了FactoryBean的类注册进 DefaultListableBeanFactory
											那么他实际上getBean的时候是调用了他的getObject方法
											beanInstance.getClass拿到Class，如果没有就通过BeanDefinition使用Class.forName拿到Class但不实例化
											getType("x1") -> mbd(factory)   ->  getTypeForFactoryBean(beanName, mbd)-> getTypeForFactoryBean -> TestBean.class
											getType("&x1") -> mbd(factory)  -> beanClass
											getBeanNamesForType 获取 Spring 容器中指定类型的所有 JavaBean 对象，遍历beanDefinitionNames
											containsBean 去beanDefinitionMap中找，支持工厂和普通
											isSingleton 从单例缓存中找，找不到则从mergedBeanDefinitions中取得相关问题
											isTypeMatch(), todo 搞不懂一点
		SimpleInstantiationStrategy，

		 * 通过指定的回调方法去创建bean实例，Spring5.0版本之后新增的方法。
		 *  可以通过实现BeanFactoryPostProcessor接口来进行扩展，设置自定义的Supplier，通过自定义supplier实例化对象
			例如重写getEarlyBeanReference


		 * 如果当前bean指定了对应的工厂方法，则通过工厂方法去创建bean实例
		 *   底层会获取工厂方法【静态工厂方法|实例化方法】--> 然后解析方法入参 --> 然后执行反射调用创建实例 --> 封装为包装对象返回.

		获取到了构造函数 || 注入模式为使用构造函数，则通过构造函数

		如果上述情况都没有：没有创建bean的回调方法 && 没有工厂方法 && 构造函数的参数未解析完毕 && 没有预先指定的默认构造函数

		则使用默认策略来创建bean对象

		默认的创建策略为：CglibSubclassingInstantiationStrategy
		即：使用Cglib的方式进行bean对象的创建
		对创建的bean对象进行包装，将创建的beanInstance赋值给BeanWrapper的wrappedObject属性
		获取的时候，通过BeanWrapper的getWrappedInstance【Object getWrappedInstance();】获取真正的bean对象
		BeanWrapper bw = new BeanWrapperImpl(beanInstance);
		对包装字后的bean对象进行初始化
		(1) 注册类型转换器，将特定类型的值转换为某一种期望的类型
		(2) 注册定制化的属性编辑器
		如果没有重写，使用获取到的构造函数反射来创建实例
		如果存在着覆写的方法，则通过CGLIB来进行实例化


		setTypeConverter: 德语 1,1 -> 1.1 等...
		setAutowireMode AUTOWIRE_BY_TYPE,从已有单例中找到，resource1，resource2 --> resourceArray
		setDependencyCheck DEPENDENCY_CHECK_OBJECTS 只涉及非简单类型的检查

		autowire 得到一个scope为prototype的db，直接实例化，getBeanDefinitionCount不增加
				若设置了通过构造器注入，AUTOWIRE_CONSTRUCTOR，构造方法中的参数一般通过类型注入，但是可以通过@Qualifier 指定注入Bean
				构造器 有限采用参数数量最多的构造函数

			lbf.setDependencyComparator(AnnotationAwareOrderComparator.INSTANCE);


		Priority注解，setPrimary的优先级更高

		ObjectProvider中重写了foreach，可以直接得到实例

		应用到实例上
		lbf.applyBeanPropertyValues(tb, "test");只关注属性
		configureBean;提供了一个更完整的配置流程，确保了 bean 在使用前被正确地配置和初始化
		spring默认非懒加载，所以如果不设置，preInstantiateSingletons直接会加载

		getConstructorArgumentValues().addGenericArgumentValue("java.lang.String");添加构造参数，可以自动转换list到数组

		工厂实例化后，产物的实例化取决于
		是否重写(需要继承smart工厂)
		@Override
		public boolean isEagerInit() {
			return true;
		}


		定义创建方法名
		RootBeanDefinition factoryMethodDefinitionWithProperties = new RootBeanDefinition();
		factoryMethodDefinitionWithProperties.setFactoryBeanName("factoryBeanInstance");
		factoryMethodDefinitionWithProperties.setFactoryMethodName("create");


		BeanDefinitionBuilder -> 封装了一下构造函数的索引，其他的好像没什么意义
				this.beanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(
				this.constructorArgIndex++, value);

		addBeanPostProcessor增加后处理器


		ReflectionUtilsTests -> ReflectionUtils 提供了递归的查找field
								重写makeAccessible，如果字段不是公开的，或其声明的类不是公开的，或字段被声明为 final，同时这个字段当前不可访问
								重写invokeMethod，适配可变参数
								提供判断异常是否可以声明的入口
								提供浅copy功能
								findMethod递归查找方法
								判断是否为cglib的方法
								getUniqueDeclaredMethods不重名方法

		StopWatch

		BridgeMethodResolver 桥接方法解析todo
		ResolvableType todo

		后处理器环节 Member是Field和Method的共同父类
		AutowiredAnnotationBeanPostProcessor
			doCreateBean调用applyMergedBeanDefinitionPostProcessors方法
				调用postProcessMergedBeanDefinition方法合并bean定义
					由autowired后处理器实现，调用findAutowiringMetadata，
						再调用buildAutowiringMetadata，放到injectionMetadataCache注入元数据缓存中，并赋值给InjectionMetadata类
							再调用InjectionMetadata的checkConfigMembers
								调用beanDefinition.registerExternallyManagedConfigMember(member)将查找到的元数据设置到Bean定义中
			回到createBean方法，populateBean
				获取当前正在实例化的bean的所有属性及值，mbd.getPropertyValues()，mbd.getResolvedAutowireMode()得到0，
					如果是按照类型注入或者按照name注入
						进入autowireByType
							进入unsatisfiedNonSimpleProperties方法，
								进入BeanWrapper的getPropertyDescriptors
									进入getCachedIntrospectionResults得到cachedIntrospectionResults
										查看缓存cachedIntrospectionResults，无则进入CachedIntrospectionResults.forClass(bean的class)
											调用CachedIntrospectionResults(beanClass)
												调用由SimpleBeanInfoFactory提供的getBeanInfo
													PropertyDescriptorUtils.determineBasicProperties(beanClass)
														调用method.getName()方法，得到get set is三个方法
															再调用StringUtils.uncapitalizeAsProperty将开头小写
																调用BasicPropertyDescriptor的构造函数，传入字段name
																	调用一段用Kotlin写的东西传入set get方法
																		将字段的属性赋值给PropertyDescriptor,
												getBeanInfo完成后循环得到的字段
													调用buildGenericTypeAwarePropertyDescriptor
														返回一个GenericTypeAwarePropertyDescriptor
															处理一些桥接的方法，恢复范型的范型擦除和桥接方法todo
															如果写不存在，读存在，去找桥接方法
															如果写方法存在而读方法不存在，得到一些歧义方法todo
															如果读方法存在，存入ResolvableType包装类和真正类
												进入introspectInterfaces内省一下接口，propertyDescriptors加入接口字段的信息
													由buildGenericTypeAwarePropertyDescriptor获得
												进入introspectPlainAccessors，处理非传统命名方式的get
										回到getPropertyDescriptors得到封装好的字段信息集合
									返回到unsatisfiedNonSimpleProperties，得到一个string的集合，包含了字段的name
									循环这个name集合
										根据名字得到PropertyDescriptor
											使用工具类BeanUtils.getWriteMethodParameter得到方法参数
											得到依赖项desc
											使用resolveDependency解析依赖项，得到装配的参数
												使用initParameterNameDiscovery
													写入getParameterNameDiscoverer()，得到两个参数名发现者
														Kotlin反射参数名发现者和标准反射名发现者
												一系列莫名其妙的判断，进入doResolveDependency
													第 1 步：预解析的单个 Bean 匹配快捷方式，即缓存
													第 2 步：预定义的值或表达式，例如来自@Value
													第 3 步：正常的注入或者别名
													步骤 4a：多个 Bean 作为流/数组/标准集合/普通映射以及其他集合类型
														查看是否继承StreamDependencyDescriptor
														查看类型是否为isArray
															进入findAutowireCandidates寻找候选项
																进入BeanFactoryUtils.beanNamesForTypeIncludingAncestors
																	进入lbf.getBeanNamesForType得到字段
															循环这些字段
																进入lbf的isAutowireCandidate查看是否有这个候选项
																调用addCandidateEntry
																	进入resolveCandidate，其中实现就是getBean
		 												返回到matchingBeans中
														进行类型转换convertIfNecessary 看不懂
														增加依赖比较器 Order注解 adaptDependencyComparator
													解析完成返回一个multipleBeans
													将其增加到pvs中
													注册进去registerDependentBean
														增加dependentBeanMap和dependenciesForBeanMap的缓存
									加入到pvs中
								退出autowireByType，进入applyPropertyValues
									经过一大串莫名其妙的代码，最终到达BeanWrapperImpl的setValue
										通过反射调用set方法，进行注入
							进入autowireByName


						依次执行后置处理器的postProcessProperties，findAutowiringMetadata找到原数据
							调用InjectionMetadata的inject方法
								调用InjectedElement的inject方法
									去单例工厂注册依赖bean，可以df中可以用getDependenciesForBean得到这些数据
										最后用反射完成注入，如果使用autoWired。默认使用以类型注入
				destroySingleton会删除被依赖的类，a依赖b，删除a则b也同时删除
				支持自定义注解
				bpp.setAutowiredAnnotationType(MyAutowired.class);
				bpp.setRequiredParameterName("optional");
				bpp.setRequiredParameterValue(false);


		Context获取bean流程，不指定构造函数，单个构造函数，无autowire注解
		调用registerBeanDefinition注册bean，可选instanceSupplier作为getBean
			refresh todo
			调用AbstractBeanFactory提供的getBean
				调用AbstractBeanFactory提供的doGetBean
					调用transformedBeanName，得到真名(解析假名和将工厂去掉&)
					判断缓存是否存在
					得到parentBeanFactory，判断是否存在
					合并bean定义
					调用DefaultSingletonBeanRegistry提供的getSingleton
						调用AbstractAutowireCapableBeanFactory提供的createBean
						通过resolveBeanClass用beanname和mdb解析class，直接通过mdb.beanClass获得即可
						prepareMethodOverrides(未知todo)
						调用resolveBeforeInstantiation
							判断是否存在instantiationAwareBeanPostProcessors
								若存在调用applyBeanPostProcessorsBeforeInstantiation
									如果返回的不是null，则直接作为bean的实例返回，不进行接下来的bean标准化创建
										同样，直接执行applyBeanPostProcessorsAfterInitializationto
									若返回null，则执行doCreateBean进行标准创建
										new一个BeanWrapper包装类 todo
										如果是单例，则删除之前工厂bean缓存中的工厂bean对象，重新进行实例化.
										调用createBeanInstance
											判断是否传入过instanceSupplier
												如果有，则调用obtainInstanceFromSupplier，调用supplier.get()方法得到实例返回
											判断是否有getFactoryMethodName
												如果有，则new一个ConstructorResolver
													调用instantiateUsingFactoryMethodinit方法
														调用beanFactory.getBean返回实例
											有一坨todo
											进入determineConstructorsFromBeanPostProcessors，从初始化bean后处理器获得构造函数
												（通过继承SmartInstantiationAwareBeanPostProcessor）
											判断四种可能
												SmartInstantiationAwareBeanPostProcessors 没有返回任何候选构造器
												通过构造器自动装配
												没有为这个构造器提供特定的参数值，这通常在 XML 配置或使用 @Autowired 注解时会明确指定
												没有通过编程方式传递构造函数的参数。
												但凡存在一种，则调用autowireConstructor
											若没有，则调用mbd.getPreferredConstructors()
												先进入RootBeanDefination提供的getPreferredConstructors()查看有没有手动指定构造函数
													如果有则返回这个构造函数
												若没有，则使用BeanUtils.findPrimaryConstructor
													判断是否兼容Kotlin，若无
												则调用clazz.getConstructors()返回构造函数合集
												调用autowireConstructor
													new一个ConstructorResolver，调用autowireConstructor方法
														判断mbd.resolvedConstructorOrFactoryMethod中是否有缓存和mbd.constructorArgumentsResolved是否解析完成
															若无则开始解析，判断是否为构造函数且没有参数，成立则调用instantiate方法，传入空参
															若有参数，一顿解析先解析依赖，
																干到DefaultListableBeanFactory中的resolveDependency完成依赖项的创建，若错误则跳过此构造函数
																权重计算为查看是否宽松mbd.isLenientConstructorResolution()
																	若宽松则优先将子类赋值给父类，差距越大越好
																	若不宽松则不考虑距离，只要能赋值成功都权重固定
																得到最好的构造函数和参数的实例
													选择一个最好的构造函数，进入instantiate环节
														调用beanFactory.getInstantiationStrategy()得到实例化策略
															得到默认的CglibSubclassingInstantiationStrategy
																调用其父类SimpleInstantiationStrategy的instantiate方法
																	若无方法重写，则使用BeanUtils.instantiateClass(ctor, args)实例化
																		给予八种基本数据类型默认值
																		调用构造函数的newInstance方法实例化
								回到doCreateBean，进入applyMergedBeanDefinitionPostProcessors todo(分叉)
								若当前的bean是单例的 && 允许bean之间的循环依赖 && bean正在创建中，则执行addSingletonFactory增加早产单例工厂，返回bean
								进入populate注入（分支hasInstantiationAwareBeanPostProcessors）
								进入initializeBean方法，
									进入applyBeanPostProcessorsBeforeInitialization方法，循环后处理器
										判断是否实现Aware，运行invokeAwareInterfaces方法
											设置各种各样的aware
									进入invokeInitMethods方法，
										判断是否有initMethodNames，由@PostConstruct得到，有则执行即可
									进入applyBeanPostProcessorsAfterInitialization方法
										执行各个后处理器的postProcessAfterInitialization方法
					回到DefaultSingletonBeanRegistry中
						进入afterSingletonCreation，删除singletonsCurrentlyInCreation中的这个bean
					如果是单例加入单例缓存
					进入getObjectForBeanInstance方法
						判断是否为工厂单例，如果不是直接返回这个bean
						如果是工厂则进入getObjectFromFactoryBean，由FactoryBeanRegistrySupport提供
							进入doGetObjectFromFactoryBean方法，由factroy.getBean返回bean
					调用adaptBeanInstance进行类型转换

		寻找构造函数逻辑
			带有autowired才能作为候选


		LookupAnnotationTests
		lookup注解原理
		在创建bean实例的时候，调用determineConstructorsFromBeanPostProcessors寻找构造函数
			进入checkLookupMethods
				循环每个方法，找到带有lookup注解的，将注解的value和方法存入重写beanDefination的集合中
					使用cglib代理创建实例
		调用方法的时候，调用LookupOverrideMethodInterceptor内部类的intercept方法，根据不同的lookup写法进行获取实例

		AnnotationsScannerTests
			AnnotationsScanner 四种注解扫描策略
				元素为类或者方法
				DIRECT只扫描本元素
				INHERITED_ANNOTATIONS扫描本类和继承的类上的注解，并且继承类上的注解需要带有inherent注解
				SUPERCLASS扫描本类和继承的类上的注解
				TYPE_HIERARCHY扫描所有注解
			顺序为本类->接口->接口的继承接口->继承类(->接口->接口的继承接口->继承类)
			扫描方法的时候，INHERITED_ANNOTATIONS有所不同，不扫描父类，即与direct一致
		尽管可以使用@Inherited注解使得类注解可以被子类继承，但这并不适用于方法注解。
		这意味着，即使一个方法注解被标记为@Inherited，这个注解也不会从父类自动传递到子类覆盖的同名方法上。
		这是Java语言的规定，Spring遵循这一规则以保持一致性。
		同时，还支持范型的桥接方法
		AnnotationConfigApplicationContext:


		ConfigurationClassPostProcessorTests -> ConfigurationClassPostProcessor原理概述
		入口postProcessBeanFactory
			进入processConfigBeanDefinitions
				将CONFIGURATION_CLASS_ATTRIBUTE设置为full，若设置(proxyBeanMethods = false)则为lite
				找到configuration注解的类，找到带有bean注解的方法，根据类设置他们的创建方法，包括排序和递归解析
			进入enhanceConfigurationClasses
				找到那个config注解并且full的类，对其使用cglib进行代理
			当需要实例化一个configuration中的bean的时候，会找到他的创建方法，触发cglib代理的拦截方法/不用代理
				如果是Spring在调用这个方法,那么就去真正执行该方法，否则,则尝试从容器中获取该 Bean 对象

		结合源码详解(关键步骤和节点) 未涉及：懒加载/AliasFor/属性重写/
		postProcessBeanFactory
			processConfigBeanDefinitions阶段
				得到所有的registry.getBeanDefinitionNames()
					循环所有的name,进入checkConfigurationClassCandidate
						如果有@Configuration注解,
		、					没有（proxyBeanMethods = false）
								为deanDef添加属性 CONFIGURATION_CLASS_FULL,表示是一个完全配置类.
							有（proxyBeanMethods = false）
								设置CONFIGURATION_CLASS_LITE
						如果没有@Configuration但是有@Bean/@Import等注解,
							则为beanDef添加属性 CONFIGURATION_CLASS_LITE,表示是一个简单的配置类.
						并且设置order
						如果都没有，则返回false
						在configCandidates添加这个bean定义
						对configCandidates进行排序
						确定 bean name生成器		@todo
						设置environment			@todo
						创建@Configuration 的解析器
						将所有待解析的数组放在set集合中,达到去重的目的
						开始递归解析
							进入解析器parse的parse方法
								进入AbstractBeanDefinition分支(此测试类为类上的configuration注解)
									进入parse的processConfigurationClass
										通过@Conditional注解判断是否跳过此类的解析 @todo
											递归调用asSourceClass方法来解析当前配置类及父配置类中的元数据信息，包括配置类上面的注解信息
												@todo
											进入doProcessConfigurationClass解析具体的配置类
												处理@Component注解和@Configuration注解
												处理@PropertySource注解
												处理@ComponentScan注解或者@ComponentScans注解，并将扫描包下的所有bean转换成填充之后的ConfigurationClass
													先找直接注解的
													再找复合注解
														进入this.componentScanParser.parse
														扫描出这个包及子包下的class，然后将其解析为BeanDefinition。
															一系列处理后进入doScan，传入basePackages
															循环解析出来的东西，判断是否为配置类，是则递归解析
												处理@Import注解。其中会调用子类中的selectImports方法，获取导入的Bean组件的名称.
												处理@ImportResource注解
												处理配置类中标注有@Bean注解的方法.
													进入retrieveBeanMethodMetadata
														getAnnotatedMethods(Bean.class.getName()得到带有bean注解的方法
															删除重复的bean创建方法，如果是size为1直接返回即可
													调用configClass.addBeanMethod增加bean方法
													进入processInterfaces，处理接口的默认实现方法，jdk8之后，接口中的方法也可以有默认实现。所以默认接口中默认实现的方法上也可能标注有@Bean注解
														递归进入processInterfaces，若是抽象的接口，则addBeanMethod
													如果有父类存在，则递归解析父类
												加入configurationClasses
								进入deferredImportSelectorHandler.process()		@todo
							进入validate做一些配置类的基本校验
								遍历所有的configurationClasses，调用他们自己的validate方法
									得到他的proxyBeanMethods和enforceUniqueMethods属性
										遍历这个类的所有方法，并调用他的beanMethod.validate
											返回类型为void则报错，静态不报错
											proxyBeanMethods为true，但是方法不可以重写报错 @todo
												不可以重写的判断为 !isStatic() && !isFinal() && !isPrivate()
											判断enforceUniqueMethods
												如果为true，检查是否有重名的工厂，如果为false则不检查
							创建configClasses，准备解析configClass中的配置类元数据
							new一个ConfigurationClassBeanDefinitionReader
							使用这个reader加载loadBeanDefinitions
								遍历configClasses进入loadBeanDefinitionsForConfigurationClass
									判断conditional
									判断isImported
										进入registerBeanDefinitionForImportedConfigurationClass @todo
									遍历getBeanMethods()
										进入loadBeanDefinitionsForBeanMethod
											别名处理
											判断是否isOverriddenByExistingDefinition，是则直接返回 @todo
											判断isStatic
												如果是StandardAnnotationMetadata
													则setBeanClass config类
												如果不是
													则设置setBeanClassName configClass.getMetadata().getClassName()
											如果不是static
												设置setFactoryBeanName为configClass.getBeanName()
											setUniqueFactoryMethodName	工厂方法
											解析注解并设置autowireCandidate defaultCandidate bootstrap initMethod destroyMethod Scope
											调用registerBeanDefinition注册上这个bean
										loadBeanDefinitionsFromImportedResources
										loadBeanDefinitionsFromRegistrars
			enhanceConfigurationClasses阶段
				循环beanFactory.getBeanDefinitionNames()
					如果BeanDefinition中的beanClass取出判断，如果只是一个String，就去forname创建
						如果是一个clss，就没事了
					如果是FullConfigurationClass,则放到变量configBeanDefs中
						循环configBeanDefs
							创建ConfigurationClassEnhancer用于加强configClass
								进入enhancer.enhance(configClass, this.beanClassLoader);
									设置一系列增强，看不太懂，只用知道beanClass被增强
										BeanMethodInterceptor 拦截 @Bean 方法的调用,以确保正确处理@Bean语义
										BeanFactoryAwareMethodInterceptor 拦截 BeanFactoryAware#setBeanFactory 的调用
		getBean阶段
			一顿基本操作到createBeanInstance
				判断存在mbd.getFactoryMethodName()
					调用instantiateUsingFactoryMethod
						取出String factoryBeanName = mbd.getFactoryBeanName();
						判断是否存在
							若存在则说明不是静态方法
								取出factoryBean = this.beanFactory.getBean(factoryBeanName)
									如果设置了（proxyBeanMethods = false）,则返回的是非增强的代理对象，反之是增强的代理对象
								设置factoryBean工厂bean，使用代理
							若不存在则说明是静态方法
								不设置工厂bean，不使用代理
								都调用SimpleInstantiationStrategy方法
									得到setFactoryBeanName
										若不存在，则说明是static，在接下来的invoke的调用实例参数传入null
										若存在，则说明不是static，在接下来的invoke的调用实例参数传入这个工厂bean，如果没有则去创建
									调用factoryMethod.invoke返回实例
										若有代理，则进入intercept拦截
											判断isCurrentlyInvokedFactoryMethod
												如果是直接调用@Bean方法,也就是Spring来调用我们的@Bean方法,则返回true
												如果是其他方法内部，则返回false
											true则调用方法，false则从容器中取





		Order在数组中的使用，原理是在解析数组候选项后排序，并且继承比较器，得到order的值作为比较器的比较内容
		primary，在解析依赖得到候选项中查找是否有primay

		DefaultListableBeanfactory中的DependencyObjectProvider，通过以下方法，在创建bean的解析依赖中，判断到objectFactory，则注入blbf中的内部类
		这使得开发者能够在应用程序运行时根据需要获取Bean
		 @Autowired
		 @Qualifier("testBean")
		 private ObjectFactory<?> testBeanFactory;
		 原理，创建DependencyObjectProvider后，调用getObject，调用doResolveDependency，循环注解信息，得到qualifier注解内容，查找对应的bean


		 CommonAnnotationBeanPostProcessorTests
		 	-> CommonAnnotationBeanPostProcessor
		 与Autowired类似，但是findResourceMetadata有一点不同，又CommonAnnotationBPP提供
		 	处理三种注解
		 		package jakarta.ejb;的EJB
		 		package jakarta.annotation;的Resource
		 		package javax.annotation;的Resource
		 	其他操作与Autowired注解几乎相同

		 AnnotationConfigApplicationContext
		 	定义AnnotatedBeanDefinitionReader
		 		最终调用registerAnnotationConfigProcessors
		 			加入后处理器
		 				ConfigurationClassPostProcessor
		 				AutowiredAnnotationBeanPostProcessor
		 				CommonAnnotationBeanPostProcessor
		 				检查 JPA 支持，如果存在，添加 PersistenceAnnotationBeanPostProcessor
		 				EventListenerMethodProcessor

		 DefaultEventListenerFactory

		 ApplicationContextEventTests
		 监听源码讲解
		 进入publishEvent(ApplicationEvent event)
		 	判断发进来的事件是否继承了事件
		 		如果继承了，则是正常的事件发布
					使用 ResolvableType.forInstance(applicationEvent)解析类型传入eventType
		 		如果没有继承，使用typeHint捏一个PayloadApplicationEvent出来，作为事件的载体
		 		进入multicastEvent方法
		 			进入getApplicationListeners(event, type)方法找到所有满足条件的事件监听器
		 			创建缓存关键字cacheKey，参数为eventType和sourceType
					去retrieverCache中根据这个key寻找，找到对应的CachedListenerRetriever
		 				如果有，则调用CachedListenerRetriever的getApplicationListeners
			 				得到applicationListeners和applicationListenerBeans，其中后者现场创建
		 				如果没有，new一个CachedListenerRetriever，加入缓存
		 					进入retrieveApplicationListeners处理
		 						循环listeners
		 							进入supportsEvent
		 								new一个GenericApplicationListenerAdapter
		 									进入resolveDeclaredEventType
		 										查看缓存并进入resolveDeclaredEventType
		 											通过ResolvableType的getGeneric得到范型中的类型，即listener中的事件类型，赋值给declaredEventType
		 								判断supportsEventType，
		 									判断是否继承GenericApplicationListener
		 										如果是继承的GenericApplicationListener，则他进入supportsEventType，其中的resolveTpye支持ResolvableType
		 										如果继承了SmartApplicationListener，则进入supportsEventType，其中的resolveTpye只能用event
		 										通过declaredEventType的isAssignableFrom判断是否实现某个接口
		 									若是eventLister注解
		 										进入ApplicationListenerMethodAdapter的supportsEventType
		 											循环resolveDeclaredEventTypes
		 												判断触发的eventType是否有未解析的类
		 													如果有则得到主体类declaredEventType.toClass()，进行isAssignable判断
		 													如果无则直接进行isAssignable判断即可
		 												如果都没有通过，则判断触发的事件是否是PayloadApplicationEvent
		 													如果是，则解析出这个类，判断isAssignable
		 														如果解析出类是null，则直接通过即可
		 								判断supportsSourceType，通过继承SmartApplicationListener
		 									并且重写supportsSourceType接口做判断
								循环listenerBeans
		 							进入supportsEvent ****(此处可以得到由methodListener注解创建的adapter)
		 								进入getType，其中需要创建实例，再得到tpye
		 									如果是GenericApplicationListener或者SmartApplicationListener则直接通过
		 									进入supportsEvent(Class<?> listenerType, ResolvableType eventType),
		 										进入GenericApplicationListenerAdapter.resolveDeclaredEventType(listenerType);
		 											判断是否为子类
					 			避免重复，确保代理和其目标不会都添加到监听器列表
		 						进入contains判断是否已经添加过
		 							并且进入supportsEvent(listener, eventType, sourceType)判断是否源也支持
		 								加入allListeners
		 						对所有监听器进行排序
		 					得到所有的listener
		 					判断是否有setTaskExecutor存在，用于异步执行监听器的事件处理，通过setTaskExecutor设置
		 						如果没有，则直接进入invokeListener
		 							寻找errorHandler	: 通过setErrorHandler设置
		 								若无，则进入doInvokeListener调用listener.onApplicationEvent
		 								若有，则再外面包裹一层errorHandler.handleError(err);
						寻找父类this.parent != null
		 					判断是否是AbstractApplicationContext
		 						如果是，则执行publishEvent(event, typeHint)
		 						如果不是，则执行重写过的publishEvent


		 eventListener注解讲解(publish开始)
		 ApplicationListenerMethodAdapterTests





		 监听器注册原理，从createBean开始
		 	进入applyMergedBeanDefinitionPostProcessors(mbd, beanType, beanName);
		 		进入ApplicationListenerDetector
		 			执行singletonNames将beanname和listener传入这个map
		 		进入initializeBean
		 			进入applyBeanPostProcessorsAfterInitialization
		 				循环getBeanPostProcessors
		 					执行addApplicationListener添加listener



		 eventListener注解原理
			进入refresh的beanFactory.preInstantiateSingletons();
		 		循环beanNames
		 			得到类型为SmartInitializingSingleton的实例
		 				执行afterSingletonsInstantiated方法
		 					循环beanNames，进入processBean方法
		 						找到那个带有eventListener注解的方法
		 						执行DefaultEventListenerFactory的factory.createApplicationListener(beanName, targetType, methodToUse)
		 							new ApplicationListenerMethodAdapter(beanName, type, method);
		 								解析BridgeMethodResolver.findBridgedMethod(method);解决两种桥接方法的情况
		 								进入AnnotatedElementUtils.findMergedAnnotation寻找EventListener注解
		 								进入resolveDeclaredEventTypes找到监听的类，优先解析注解，支持多个，两种写法
		 								如果注解没有，则通过ResolvableType.forMethodParameter解析形参
		 						加入监听器addApplicationListener

		 StandardEnvironmentTests->
			new一个StandardEnvironment
		 		直接跳到new一个AbstractEnvironment，不过可以选择source
		 			new一个MutablePropertySources()
		 				进入MutablePropertySources(PropertySources propertySources)
		 					设置propertySources
		 					设置源解析器ConfigurablePropertyResolver propertyResolver
		 					进入customizePropertySources设置定制化的初始化源
		 						StandardEnvironment的具体实现是  :此二者都是传入了一个map的地址，所以可以动态变化
		 							设置PropertiesPropertySource System.getProperties();
		 							设置SystemEnvironmentPropertySource
		 								判断是否suppressGetenvAccess
		 									即是否设置了System.setProperty("spring.getenv.ignore", "true");
		 									System.getenv();

		getActiveProfiles
			进入doGetActiveProfiles
				进入doGetActiveProfilesProperty
					如果activeProfiles是非空的		此处可以通过environment.setActiveProfiles("local", "embedded");进行设置
						则直接返回
					如果是空的
						则进入getProperty("spring.profiles.active")		此处可以通过System.setProperty(ACTIVE_PROFILES_PROPERTY_NAME, " bar , baz ");进行设置
							进入this.propertyResolver.getProperty(key);通过解析器解析
							将得到的Str切割，放入activeProfiles

		addActiveProfile
			进入doGetActiveProfiles
				遍历add进去

		getDefaultProfiles
			进入doGetDefaultProfiles
				如果没有改变defaultProfiles	(它的初始化就是一个default)
					则去getProperty(spring.profiles.default)中寻找
				如果改变了，即不是default，则直接返回即可

		setDefaultProfiles同理，无add方法

		isProfileActive方法
			先进入validateProfile判断
				非空且不是"!"开头
			通过doGetActiveProfiles得到currentActiveProfiles
				判断是否contain这个profile
					如果不包含，并且为空
						则去doGetDefaultProfiles中找，查看是否contain

		acceptsProfiles方法
			循环传入的string数组
				如果是 ! 开头,则!isProfileActive成立即true
				如果不是 ! 开头,则isProfileActive成立则true
			循环完成都没有true则返回false


	 StandardEnvironment()





		 GenericApplicationContextTests
		 	GenericApplicationContext
		 		初始化就是一个dlbf
		 refresh方法






	 * @author wangbin33
	 * @date Created in 18:45 2019/10/6
	 * 注解式AOP原理分析：【看容器中注册了什么组件，这个组件什么时候工作，这个组件的功能是什么？】
	 * 	@EnableAspectJAutoProxy:
	 * 	1.@EnableAspectJAutoProxy原理？
	 * 	 	@Import(AspectJAutoProxyRegistrar.class): 给容器中导入AspectJAutoProxyRegistrar
	 *         利用AspectJAutoProxyRegistrar自定义给容器中注册Bean.
	 *		    org.springframework.aop.config.internalAutoProxyCreator=AnnotationAwareAspectJAutoProxyCreator
	 *         给容器中注册一个AnnotationAwareAspectJAutoProxyCreator.
	 *	2.AnnotationAwareAspectJAutoProxyCreator：
	 *	(1)继承关系：
	 *		AnnotationAwareAspectJAutoProxyCreator
	 *			-> 	继承了AspectJAwareAdvisorAutoProxyCreator
	 *				-> 继承了AbstractAdvisorAutoProxyCreator
	 *					-> 继承了AbstractAutoProxyCreator
	 *						-> 实现了(implements)SmartInstantiationAwareBeanPostProcessor(Bean的后置处理器)接口和BeanFactoryAware接口
	 *					    关注该后置处理器(在Bean初始化前后执行)，自动装配BeanFactory.
	 *	(2)分析后置处理器和Aware接口方法中的执行过程
	 *		AbstractAutoProxyCreator.setBeanFactory 设置bean工厂.
	 *		AbstractAutoProxyCreator.postProcessBeforeInstantiation 后置处理器逻辑.
	 *
	 *		AbstractAdvisorAutoProxyCreator.setBeanFactory() 重写了setBeanFactory，所以调用的应该是子类的setBeanFactory方法
	 *			setBeanFactory方法中调用了 initBeanFactory()方法
	 *
	 * 		AspectJAwareAdvisorAutoProxyCreator中没有和aware接口和后置处理器相关的方法.
	 *
	 *		AnnotationAwareAspectJAutoProxyCreator.initBeanFactory() 该方法在AbstractAdvisorAutoProxyCreator类的setBeanFactory中调用了，
	 *	 	  但是又被子类重写了，所以父类中调用initBeanFactory方法的时候，调用的是本类中的initBeanFactory方法.
	 *
	 *
	 *	3.执行流程：
	 *		(1)通过配置类，创建IOC容器
	 *		(2)注册配置类，调用refresh方法
	 *		(3)registerBeanPostProcessors()，注册Bean的后置处理器，来拦截bean的创建.
	 *			(1)先获取IOC容器中已经定义的需要创建对象的BeanPostProcessor
	 *			(2)给容器中添加其他的BeanPostProcessor
	 *			(3)优先注册实现了PriorityOrdered接口的BeanPostProcessor
	 *			(4)再给容器中注册实现了Ordered接口的BeanPostProcessor
	 *			(5)最后注册没实现优先级接口的BeanPostProcessor
	 *			(6)注册BeanPostProcessor，实际上就是创建BeanPostProcessor对象，并保存到容器中.
	 *				创建名称为：org.springframework.aop.config.internalAutoProxyCreator类型为AnnotationAwareAspectJAutoProxyCreator的bean对象.
	 *				(1)创建Bean的实例
	 *				(2)populateBean(beanName, mbd, instanceWrapper)，被bean的属性赋值.
	 *				(3)initializeBean(beanName, exposedObject, mbd)，初始化Bean.
	 *					初始化Bean
	 *					(1)invokeAwareMethods(beanName, bean)，处理Aware接口的方法回调
	 *					(2)applyBeanPostProcessorsBeforeInitialization(wrappedBean, beanName)，应用后置处理器的postProcessBeforeInitialization(result, beanName)
	 *					(3)invokeInitMethods()，执行自定义的初始化方法，例如：init-method或者destroy-method
	 *					(4)applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName)，应用后置处理器的postProcessAfterInitialization(result, beanName)
	 *				(4)AnnotationAwareAspectJAutoProxyCreator创建成功，并创建了一个包装类aspectJAdvisorsBuilder
	 *			(7)把BeanPostProcessor注册到BeanFactory中
	 *				beanFactory.addBeanPostProcessor(postProcessor)
	 *	//////////////////////以上是创建AnnotationAwareAspectJAutoProxyCreator的过程////////////////////////
	 *		AnnotationAwareAspectJAutoProxyCreator =>继承了 InstantiationAwareBeanPostProcessor 接口.
	 *		(4)finishBeanFactoryInitialization(beanFactory)，完成BeanFactory初始化，创建剩余的单实例Bean.
	 *			(1)遍历获取容器中所有的Bean，依次创建对象getBean(beanName)
	 *				getBean(beanName) -> doGetBean() -> getSingleton()
	 *			(2)创建bean
	 *				【AnnotationAwareAspectJAutoProxyCreator会在所有的Bean创建之前有一个拦截，因为它属于InstantiationAwareBeanPostProcessor类型的后置处理器，
	 *					而该种处理器就是在Bean创建之前执行。会调用postProcessBeforeInstantiation方法】
	 *
	 *				(1)先从缓存中获取当前bean，如果能获取到，说明bean是之前被创建过的，直接使用；否则再去获取；
	 *				   只要创建好的Bean，都会被缓存起来.
	 *
	 *				(2)createBean()，创建Bean对象。注意：AnnotationAwareAspectJAutoProxyCreator会在任何Bean创建之前返回Bean的实例
	 *					【BeanPostProcessor】是在Bean实例创建完成，初始化前后调用的
	 *					【InstantiationAwareBeanPostProcessor】是在创建Bean实例之前先尝试用后置处理器返回对象的.
	 *					(1)resolveBeforeInstantiation(beanName, mbdToUse)，解析BeforeInstantiation
	 *					   希望后置处理器在此可以创建一个代理对象；如果能返回代理对象就返回，如果不能则继续向下执行doCreateBean
	 *					   (1)后置处理器先尝试返回对象
	 *					   		// 拿到所有的后置处理器，如果是InstantiationAwareBeanPostProcessor，则执行后置处理器的postProcessBeforeInstantiation方法
	 *					   		bean = applyBeanPostProcessorsBeforeInstantiation(targetType, beanName);
	 *					   		//
	 *					   		if (bean != null) {
	bean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
	}
	 *					(2)doCreateBean(beanName, mbdToUse, args)，真正去创建一个Bean实例，和上述步骤创建Bean实例的步骤相同.
	 *
	 *		AnnotationAwareAspectJAutoProxyCreator【InstantiationAwareBeanPostProcessor】的作用：
	 *		(1)在每一个Bean创建之前，调用postProcessBeforeInstantiation方法
	 *			关心MathCalculator和LogAspect的创建
	 *			(1)先判断当前Bean是否在advisedBean中(advisedBeans中保存了所有需要增强的bean)
	 *			(2)使用isInfrastructureClass(Class<?> beanClass)方法判断当前bean是否为基础类型的，
	 *					即：是否实现了Advice，Pointcut，Advisor，AopInfrastructureBean接口。
	 *				或者是否为切面:aspectJAdvisorFactory.isAspect(beanClass)，加了@Aspect注解的.
	 *			(3)判断是否需要跳过.
	 *				(1)获取候选的增强器(切面里面的通知方法)【List<Advisor> candidateAdvisors = findCandidateAdvisors()】
	 *					每一个封装的通知方法的增强器是InstantiationModelAwarePointcutAdvisor，
	 *					判断逻辑中判断每一个增强器是否为AspectJPointcutAdvisor类型的 (if (advisor instanceof AspectJPointcutAdvisor) )
	 *				(2)返回false，跳过.
	 *		(2)创建对象
	 *			调用postProcessAfterInitialization方法：
	 *				该方法中调用wrapIfNecessary(bean, beanName, cacheKey)方法
	 *				(1)调用getAdvicesAndAdvisorsForBean(bean.getClass(), beanName, null)方法获取Bean的所有增强器
	 *					(1)调用findAdvisorsThatCanApply方法找到所有候选的Bean增强器（找哪些通知方法需要切入当前Bean方法的）
	 *					(2)获取到能在当前Bean中使用的增强器
	 *					(3)给增强器排序
	 *				(2)记录当前已经被增强的Bean到advisedBeans中
	 *				(3)如果当前Bean需要增强，则创建当前Bean的代理对象.
	 *					(1)获取所有的增强器（通知方法）
	 *					(2)保存到proxyFactory中.
	 *					(3)创建代理对象，Spring自动决定创建哪种代理.
	 *						JdkDynamicAopProxy(config)，jdk动态代理
	 *						ObjenesisCglibAopProxy(config)，cglib动态代理
	 *				(4)给容器中返回当前组件使用Cglib增强之后的代理对象.
	 *				(5)以后容器中获取到的就是组件的代理对象，执行目标方法的时候，代理对象就会执行通知方法的流程.
	 * 		(3)目标方法执行
	 *			容器中保存了组件的代理对象(cglib增强之后的对象)，这个对象里面保存了详细信息(比如：增强器，目标对象等)
	 *			(1)CglibAopProxy.intercept方法；拦截目标方法的执行
	 *			(2)根据ProxyFactory对象获取目标方法的拦截器链
	 *				List<Object> chain = this.advised.getInterceptorsAndDynamicInterceptionAdvice(method, targetClass)
	 *				(1)创建一个保存拦截器器链的List，其中的元素包括默认的ExposeInvocationInterceptor和其他自定义的增强器.
	 *				(2)遍历所有的增强器，将其包装为一个Interceptor；通过registry.getInterceptors(advisor)方法包装
	 *				(3)将增强器转为MethodInterceptor，并封装到List<MethodInterceptor>中
	 *					如果是MethodInterceptor，直接加入到List中
	 *					如果不是MethodInterceptor，使用AdvisorAdapter（增强器的适配器）将增强器转为MethodInterceptor
	 *					将转换完成的MethodInterceptor集合返回.
	 *			(3)如果没有拦截器链，直接执行目标方法
	 *				拦截器链: 每一个通知方法又被包装为方法拦截器，利用MethodInterceptor机制
	 *			(4)如果有拦截器链，把需要执行的目标对象，目标方法，拦截器链等信息传入创建一个CglibMethodInvocation，并调用proceed方法，得到返回值.
	 *				Object retVal = new CglibMethodInvocation(proxy, target, method, args, targetClass, chain, methodProxy).proceed()
	 *			(5)拦截器链的触发过程.
	 *				(1)如果没有拦截器，直接执行目标方法。或者拦截器的索引和拦截器的长度减1相等（即到了最后一个拦截器）
	 *				(2)链式获取每一个拦截器，拦截器执行invoke方法，每一个拦截器等待下一个拦截器执行完成以后再来执行；
	 *					通过拦截器链的机制，保证通知方法与目标方法的执行顺序.
	 *
	 *
	 * 	总结：
	 * 		1.@EnableAspectJAutoProxy 开启AOP功能
	 * 		2.@EnableAspectJAutoProxy会给容器中注册一个类型为AnnotationAwareAspectJAutoProxyCreator的组件
	 * 		3.AnnotationAwareAspectJAutoProxyCreator是一个后置处理器
	 * 		4.容器的创建流程:
	 * 			(1)registerBeanPostProcessors会注册后置处理器，创建AnnotationAwareAspectJAutoProxyCreator对象
	 * 			(2)finishBeanFactoryInitialization，初始化剩余的单实例Bean
	 * 				(1)创建业务逻辑组件和切面组件
	 * 				(2)AnnotationAwareAspectJAutoProxyCreator会拦截组件的创建过程
	 * 				(3)在组建创建完成之后，会判断组件是否需要增强
	 * 					如果是：切面中的包装方法，包装为增强器(Advisor)，给业务逻辑组件创建代理对象(默认是cglib，如果有接口，使用jdk)
	 * 		5.执行目标方法:
	 * 			(1)代理对象执行目标方法
	 * 			(2)CglibAopProxy.intercept()
	 * 				(1)得到目标方法的拦截器链(增强器包装为拦截器MethodInterceptor)
	 * 				(2)利用拦截器链的链式机制，依次进入每一个拦截器进行执行
	 * 				(3)执行效果：
	 * 					正常执行：前置通知->目标方法->后置通知->返回通知
	 *					异常执行：前置通知->目标方法->后置通知->异常通知
 *

			AbstractApplicationContext
\

			AnnotationConfigApplicationContext

			ClassPathXmlApplicationContext

			事件发布暂时搁置


				分布式 10
				分布式架构 5
				Vr 推荐算法 5
				AI人工智能 王小朱 10
				请客吃饭 5
				王大师 10
				年前上线 20
				对接工商 10
				办卡 10
 				不买可乐 10
 				不买卫生纸早餐 10
 				去水印 10
 				v2.0 10
 				小区代言人 10
 				太多房源种类 10
 				经纪人排行 10
 				短视频功能 20
 				开早会 10
 				4000套房源 10
 				对接住建局 10
 				全国范围业务 10
 				去市里面汇报 10
 				对接华为 10
 				拿销售反推开发 20
 				加需求 20
 				2.0开发速度 10
 				移花接木/无中生有 30		t000
 				996无加班费 10
 				职场PUA 10
 				自动过滤 10
 				关键词检索 10
 				画饼 10
 				下班时间不让走 10
 				看生肖/看长相/看风水 10
 				来学校抓老子 10

	 */