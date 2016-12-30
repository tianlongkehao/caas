<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE html>
<html>
<head lang="en">
<title>产品</title>
<%@include file="../frame/header.jsp"%>
<link rel="stylesheet" type="text/css" href="<%=path%>/css/mod/help.css" />
<link rel="stylesheet" type="text/css" href="<%=path%>/css/core/jquery-ui.min.css" />
<script type="text/javascript" src="<%=path%>/js/plugins/jquery-ui.min.js"></script>
</head>

<body>
	<jsp:include page="../frame/bcm-menu.jsp" flush="true">
		<jsp:param name="cluster" value="" />
	</jsp:include>

	<div class="page-container">
		<article>
			<div class="page-main">
				<div class="contentTitle">
					<ol class="breadcrumb">
						<li><a href="<%=path %>/home"><i class="fa fa-home"></i>&nbsp;&nbsp;<span
								id="nav1">控制台</span></a></li>
						<li><i class="fa fa-angle-right"></i></li>
						<li class="active" id="nav2">新手入门</li>
					</ol>
				</div>
				<div class="container" style="margin: 0px; width: 100%">
					<div class="book row book-row">
						<div class="book-summary col-md-3">

							<ul class="summary" id="book-tab" class="nav nav-tabs">

								<li class="chapter " data-level="0"><a href=""> <i
										class="fa fa-check" style="display: none;"></i> 帮助文档
								</a></li>

								<li class="chapter " data-level="1"><a
									href="#product-info1" data-toggle="tab" id="a1"> <i
										class="fa fa-check" style="display: none;"></i> <b>1.</b> 平台介绍
								</a></li>

								<li class="chapter " tabindex="0" data-level="2"><a
									href="#product-info2-0" data-toggle="tab" id="a2"><b>2.</b>
										新手指引 </a>
									<ul class="articles" role="menu" style="display: none;"
										aria-labelledby="a2" id="a2child">

										<li class="chapter " data-level="2.1"><a
											href="#product-info2-1" tabindex="-1" data-toggle="tab">
												<b>2.1.</b> 什么是容器服务
										</a></li>

										<li class="chapter " data-level="2.2"><a
											href="#product-info2-2" tabindex="-1" data-toggle="tab">
												<b>2.2.</b> 什么是微服务
										</a></li>

										<li class="chapter " data-level="2.3"><a
											href="#product-info2-3" tabindex="-1" data-toggle="tab">
												<b>2.3.</b> BONC-PaaS云平台的优势
										</a></li>


									</ul></li>

								<li class="chapter " data-level="3"><a
									href="#product-info3" data-toggle="tab" id="a3"><b>3.</b>
										容器服务 </a>
									<ul class="articles" role="menu" style="display: none;"
										id="a3child">
										<li class="chapter " data-level="3.1"><a
											href="#product-info3-1" data-toggle="tab" tabindex="-1">
												<b>3.1.</b> 创建一个容器服务
										</a></li>
										<li class="chapter " data-level="3.2"><a
											href="#product-info3-2" data-toggle="tab" tabindex="-1">
												<b>3.2.</b> 查看服务信息
										</a></li>
										<li class="chapter " data-level="3.3"><a
											href="#product-info3-3" data-toggle="tab" tabindex="-1">
												<b>3.3.</b> 停止/启动/删除容器
										</a></li>
										<li class="chapter " data-level="3.4"><a
											href="#product-info3-4" data-toggle="tab" tabindex="-1">
												<b>3.4.</b> 更改服务配置
										</a></li>
										<li class="chapter " data-level="3.5"><a
											href="#product-info3-5" data-toggle="tab" tabindex="-1">
												<b>3.5.</b> 弹性伸缩
										</a></li>
										<li class="chapter " data-level="3.6"><a
											href="#product-info3-6" data-toggle="tab" tabindex="-1">
												<b>3.6.</b> 版本升级
										</a></li>
									</ul></li>

								<li class="chapter " data-level="4"><a
									href="#product-info4" data-toggle="tab" id="a4"> <i
										class="fa fa-check" style="display: none;"></i> <b>4.</b> 构建镜像
								</a>
									<ul class="articles" role="menu" style="display: none;"
										id="a4child">
										<li class="chapter " data-level="4.1"><a
											href="#product-info4-1" tabindex="-1" data-toggle="tab"
											id="a4next1"> <b>4.1.</b> 代码构建
										</a></li>

										<li class="chapter " data-level="4.2"><a
											href="#product-info4-2" tabindex="-1" data-toggle="tab"
											id="a4next2"> <b>4.2.</b> 快速构建
										</a></li>
										<li class="chapter " data-level="4.3"><a
											href="#product-info4-3" tabindex="-1" data-toggle="tab"
											id="a4next3"> <b>4.3.</b> 上传镜像
										</a></li>
										<li class="chapter " data-level="4.4"><a
											href="#product-info4-4" tabindex="-1" data-toggle="tab"
											id="a4next4"> <b>4.4.</b> dockerfile构建
										</a></li>
									</ul></li>

								<li class="chapter " data-level="5"><a
									href="#product-info5" data-toggle="tab" id="a5"> <i
										class="fa fa-check" style="display: none;"></i> <b>5.</b> 镜像服务
								</a>
									<ul class="articles" role="menu" style="display: none;"
										id="a5child">
										<li class="chapter " data-level="5.1"><a
											href="#product-info5-1" tabindex="-1" data-toggle="tab">
												<b>5.1.</b> 浏览镜像
										</a></li>
										
										<li class="chapter " data-level="5.2"><a
											href="#product-info5-2" tabindex="-1" data-toggle="tab">
												<b>5.2.</b> 收藏镜像
										</a></li>
									</ul></li>

								<li class="chapter " data-level="6"><a
									href="#product-info6" data-toggle="tab" id="a6"> <i
										class="fa fa-check" style="display: none;"></i> <b>6.</b> 租户管理
								</a>
									<ul class="articles" role="menu" style="display: none;"
										id="a6child">
										<li class="chapter " data-level="6.1"><a
											href="#product-info6-1" tabindex="-1" data-toggle="tab">
												<b>6.1.</b> 创建租户
										</a></li>
										<li class="chapter " data-level="6.2"><a
											href="#product-info6-2" tabindex="-1" data-toggle="tab">
												<b>6.2.</b> 查看/修改/删除租户
										</a></li>

									</ul></li>

								<li class="chapter " data-level="7"><a
									href="#product-info7" data-toggle="tab" id="a7"> <i
										class="fa fa-check" style="display: none;"></i> <b>7.</b> 集群管理
								</a>
									<ul class="articles" role="menu" style="display: none;"
										id="a7child">
										<li class="chapter " data-level="7.1"><a
											href="#product-info7-1" tabindex="-1" data-toggle="tab">
												<b>7.1.</b> 集群监控
										</a></li>
										<li class="chapter " data-level="7.2"><a
											href="#product-info7-2" tabindex="-1" data-toggle="tab">
												<b>7.2.</b> 容器监控
										</a></li>
										<li class="chapter " data-level="7.3"><a
											href="#product-info7-3" tabindex="-1" data-toggle="tab">
												<b>7.3.</b> 集群管理-创建集群节点
										</a></li>
										<li class="chapter " data-level="7.4"><a
											href="#product-info7-4" tabindex="-1" data-toggle="tab">
												<b>7.4.</b> 集群拓扑
										</a></li>
									</ul></li>

								<li class="chapter " data-level="8"><a
									href="#product-info8" data-toggle="tab" id="a8"> <i
										class="fa fa-check" style="display: none;"></i> <b>8.</b> 常见问题
								</a>
									<ul class="articles" role="menu" style="display: none;"
										id="a8child">
										<li class="chapter " data-level="8.1"><a
											href="#product-info8-1" tabindex="-1" data-toggle="tab"><b>8.1.</b>
												常见问题 </a></li>

										<li class="chapter " data-level="8.2"><a
											href="#product-info8-2" tabindex="-1" data-toggle="tab">
												<b>8.2.</b> 如何编写Dockerfile
										</a></li>

									</ul></li>
								<li class="divider"></li>
							</ul>
						</div>
						<!-- book-summary -->



						<div class="book-body col-md-9">
							<div class="body-inner tab-content">

								<div class="page-wrapper tab-pane fade in active"
									id="product-info1">
									<div class="page-inner">
										<section class="normal" id="">
											<h1 id="">BONC-PaaS产品信息中心</h1>
											<p>东方国信是目前国内应用软件及大数据建设的领先者，商业智能、大数据、云计算、移动及互联网金融等产品已广泛应用于多个行业。目前已形成电信、金融、政府、工业、能源和新闻媒体六大板块业务体系，旨在打造民族软件品牌，成为中国大数据领域的领导者。</p>
											<p>BONC-PaaS云平台是由东方国信大数据研发团队研发的容器云平台和解决方案，基于以Docker为代表的容器技术，为开发者和企业提供镜像构建、发布、持续集成、集群管理的新一代云计算平台。</p>
											<p>BONC-PaaS目前的功能主要包括“容器服务”、“持续集成”、“镜像服务”和“集群管理”，涵盖了实现容器服务的整个体系架构，权衡了平台未来对公有云、私有云以及混合云不同场景的扩展支持。这里是BONC-PaaS产品的文档信息中心，涵盖产品的使用说明，案例分析，常见问题，问题定位等帮助信息，力争汇总各种有价值的信息，使用户更加容易方便的找到所需要的详细文档。</p>
											<ul class="info">
												<li>新手指引</li>
												<li>容器服务</li>
												<li>构建镜像</li>
												<li>镜像服务</li>
												<li>集群管理</li>
												<li>常见问题</li>
											</ul>
										</section>
									</div>
								</div>

								<div class="page-wrapper tab-pane fade" id="product-info2-0">
									<div class="page-inner">
										<section class="normal">
											<h1 id="新手指引">新手指引</h1>
											<p>提到虚拟化，很多人都会立刻想到虚拟机，其实它只是虚拟化的一种实现。容器是另一种虚拟化，一种操作系统级别的虚拟化。
												从本质上来说，容器就是提供一个与宿主机操作系统共享内核但与系统中的其它进程资源相隔离的执行环境。其轻量级部署运行
												和秒级启动特性，帮助开发者快速构建、发布、部署和实例化应用程序。</p>
											<p>容器化最直接的好处在于简化DevOps，当应用采用了微服务架构(Micro-services
												architecture)，每个容器就是一个微服务，
												而容器的灵活性意味着微服务可以随负载增长而快速横向扩展，且namespace(包含一个应用程序能够交互的所有资源)与资
												源隔离阻止了微服务实例之间的互相干扰。</p>
											<p>通过新手指引，您可以初步了解一下什么是容器服务，容器服务带来的好处以及为什么需要容器服务；如果你了解过传
												统的企业SOA架构，那么你也许会对微服务感兴趣，微服务是采用一组服务的方式来构建一个应用，服务独立部署在不同的
												进程中，不同服务通过一些轻量级交互机制来通信。本节我们还将为您介绍BONC-PaaS云平台基于容器技术的特征以及优势。</p>
											<ul class="info">
												<li>什么是容器服务</li>
												<li>什么是微服务</li>
												<li>BONC-PaaS云平台的优势</li>
											</ul>
										</section>
									</div>
								</div>
								<div class="page-wrapper tab-pane fade" id="product-info2-1">
									<div class="page-inner">
										<section class="normal">
											<h2 id="什么是容器服务（container-as-a-service）">什么是容器服务（Container
												as a Service）</h2>
											<p>介绍容器服务之前我们先了解一下docker：</p>
											<p>Docker是时下流行的容器技术之一，起先是基于LXC(Linux Container)的开源容器管理引擎，
												现在runC(标准化容器执行引擎，符合OCI标准的开放容器项目)更让它欣欣向荣向前发展。</p>
											<p>Docker 的基础是 Linux
												容器（LXC）等技术。与传统“重量级”的虚拟机相比，Docker在LXC之上融合AUFS分层镜像管理机制，
												抛弃传统虚拟机试图模拟完整机器的思路，而是以应用为单元进行“集装封箱”，是“轻量级”的虚拟化技术。</p>
											<p>
												下面的图片比较了 Docker
												和传统虚拟化方式的不同之处，可见容器是在操作系统层面上实现虚拟化，直接复用本地主机的操作系统，而传统方式则是在硬件层面实现。
												<img src="<%=path%>/images/containers.png" alt="container1"> </br>(图片来自Docker官方网站)
											</p>
											<p>Docker
												Engine可以自动化部署应用到可移植的的容器中，这些容器独立于硬件、语言、框架、打包系统。一个标准的Docker容器
												包含一个软件组件及其所有的依赖，包括二进制文件，库，配置文件，脚本等，实现持续集成与部署，快速迭代应用程序。</p>
											<p>Docker容器可以封装任何有效负载，几乎可以在任何服务器之间进行一致性运行。开发者构建的应用只需一次构建即可多平台
												运行。运营人员只需配置他们的服务，即可运行所有应用。</p>
											<p>Docker的终极目标是简化容器的创建，并让这些容器可以作为开发者和系统管理者标准化、配置、交付应用的最佳方案。如果说
												Docker交付运行环境如同海运，那么OS如同一个货轮，每一个在OS上的App都如同一个集装箱，用户可以通过标准化手段自由组
												装运行环境， 同时集装箱的内容可由用户自定义，也可由专业人员制造。这样，交付一个应用，就是一系列标准化组件的集合交付。</p>

											<p>有了上面对Docker容器的认识，你应该对容器服务有了一个大致的理解。BONC-PaaS云平台容器服务是采用目前流行的Docker容器，基于强大的容器集群调度工具，能轻松管理和调度成千上万个容器，做到秒级启动，秒级销毁，并且能做到大规模负载均衡集群。BONC-PaaS云平台倡导将容器化应用作为云计算应用交付的标准，这样的好处是能保持应用环境的一致性，让开发和运维等从烦杂的环境构建中解脱出来，另外还可以做到跨平台，能运行在任意安装了Docker
												Engine的平台上。BONC-PaaS云平台容器服务（Container as a
												Service）为开发者和企业提供快速构建、部署、运行容器化应用的平台，并原生支持Microservices架构。</p>
										</section>
									</div>
								</div>

								<div class="page-wrapper tab-pane fade" id="product-info2-2">
									<div class="page-inner">
										<section class="normal">

											<h2 id="什么是微服务">什么是微服务</h2>
											<p>微服务是在不同的进程中独立部署各种不同的服务，通过这些不同的服务构建出一组服务组成的一个
												应用。各个服务可独立运行，通过轻量级的交互机制通信，服务之间有各自的边界，各个服务可以采用不相同
												的编程语言开发，由独立的团队通过维护应用整体的方式来维护各个服务。</p>
											<h4 id="微服务架构特征（characteristics）">微服务架构特征（Characteristics）</h4>
											<p>1.组件化</p>
											<p>通过服务实现组件，代替了以往通过库实现组件的方式。传统方式中，组件和应用共同运行在进程中，当组件发
												生变化时，将会导致重新部署整个应用。而通过服务实现组件，组成应用的各个服务运行在不同的进程中，当某一服务
												发生变化时，只需要重新部署该服务所在的进程即可。避免了应用整体重新部署所带来的资源占用。将服务组件化，使得
												各个服务之间有明确的边界，服务之间需要跨进程才能调用，各个服务有清晰的边界和职责。</p>
											<p>2.按业务能力来划分服务与组织团队</p>
											<p>微服务架构的开发模式相较于以往的传统开发方式有很大的不同。传统开发中，我们主要依靠开发工程各自擅长的技能
												将整个开发过程分层，前端层由UI工程师，页面构建师等负责，中间层对应服务端业务开发工程师等角色，数据层则由DBA
												实现。传统开发设计架构分层更多的是依赖于工程师之间技能的不同来划分。而微服务是通过业务能力来把应用划分为不同的服
												务，各个服务均需要在对应业务领域中的全栈（从前端到后端）软件实现，其中包括界面开发，数据存储，外部沟通等。微服
												务所要求的开发团队是跨功能的，包括实现业务所需的较为全面的技能。</p>
											<p>3.服务即产品</p>
											<p>相较于传统应用开发的项目方式，微服务架构开发要求开发团队负责包括应用开发到后期维护的整个过程。传统的应用
												开发，开发团队根据业务需求完成应用的功能开发工作，将软件交付客户后，该应用由运维人员进行维护。开发团队与运维
												团队互相脱离。微服务架构使开发团队负责参与到整个产品的全部生命周期。</p>
											<p>4.智能终端与哑管道</p>
											<p>微服务架构使服务作为智能终端，所有的业务智能逻辑在服务内部处理，而服务间的通信尽可能的轻量化，不添加任何额外的业务规则。
											</p>
											<p>5.去中心统一化</p>
											<p>微服务与传统应用解决问题的方式不同。传统应用中更多的是采用统一的
												技术或者产品来解决所有问题。这种处理问题的方式，忽视了问题的差异性，对于各种不同的
												问题，用相同的方式解决是不科学，不明智的。而微服务的架构避免了这一统一化的问题，微服务可以针
												对不同的业务服务特征选择不同的技术平台或产品，对于不同的业务问题有针对性的，用最适合的方案去解决。</p>
											<p>6.基础设施自动化</p>
											<p>微服务中将传统应用的单一进程拆解成一系列多进程服务，相较于传统方式而言，微服务的开发过程显得更加
												复杂，难度也更大，因此，为了控制开发和运维成本，微服务的架构模式依赖于合适的自动化基础设施的支持。</p>
											<p>7.规避失败风险</p>
											<p>微服务将整体应用拆分成多进程服务，在获得诸多优势的同时也承担了更多额外的失败的风险。这其实就是相
												较于传统应用开发方式的一个缺点，但是随着各种开源服务化框架的出现，适当的屏蔽了类似于服务调用时由于服务方不可用等导致的失败。</p>
											<p>8.进化设计</p>
											<p>采用微服务架构模式，在服务需要变更时我们更需谨慎对待。变更服务提供者可能破坏服务使用者的兼容性，因此要时刻谨记保持服务契约（接口）的兼容性。</p>
										</section>
									</div>

								</div>
								<div class="page-wrapper tab-pane fade" id="product-info2-3">
									<div class="page-inner">
										<section class="normal">
											<h2 id="BONC-PaaS云平台的优势">BONC-PaaS云平台的优势</h2>
											<p>对“BONC-PaaS云平台”进行了具体分析，来帮助您了解该平台，以下是“BONC-PaaS云平台”产品的主要优势：</p>
											<h3 id="1-集群自动化安装部署及管理">1. 集群自动化安装部署及管理</h3>
											<p>集群管理，是“BONC-PaaS云平台”推出的最有特色的功能之一。“集群”，顾名思义，是由多个计算机组成，但不仅仅是机器的堆砌；
												作为一个整体，集群用来提供高质量不间断的服务，具有很高的容错性；而集群中的单个节点（一般指机器）实现功能上相同或者互补的服务，一旦
												宕机，可以瞬间被其它节点取代。</p>
											<p>我们通过自身平台的技术和优势为用户提供集群的部署、管理和监控的一整套解决方案。通过集群化的管理用户自己的实体主机、虚拟机或者
												云主机上的资源，合理规划和充分利用现有的计算和存储资源，并在自己的私有集群上尝试、运用镜像和容器技术，通过这些新技术推进自己的产品
												在开发、运维、部署、交付等各个环节上的变革及创新，逐渐搭建和形成自己的私有云架构。</p>

											<h3 id="2-集群及容器服务资源监控">2.集群及容器服务资源监控</h3>
											<p>对集群资源和容器服务资源使用情况进行监控，实现实时查看、筛选功能，资源监控界面简单明了，易于操作。帮助开发
												人员或管理员监控其程序服务的运行状况，并在阈值超出极值的时候，发出警告。当发现异常时，运维人员还可以钻取到那段时间的日志，展开进一步诊断。</p>

											<h3 id="3-多租户管理机制">3.多租户管理机制</h3>
											<p>Kubernetes通过用户空间（namespace）来实现了一个简单的多租户模型，然后为每一个用户空间指定一定的配额，所有用户按照租户进行逻辑上的隔
												离，Namespace能够帮助不同的租户共享同一个k8s集群。特点如下：</p>
											<ol>
												<li>将资源对象与实际的物理节点解耦，用户只需要关注namespace而非minion节点上的资源情况。</li>
												<li>建立一种简单易用，能够在逻辑上对k8s资源对象进行隔离的机制。</li>
												<li>可以与k8s的认证授权机制相结合。</li>
												<li>通过namespace对k8s的资源进行归类，使得k8s具有一套有效的过滤资源请求的机制。</li>
											</ol>
											<h3 id="4-开发运维自动化解决方案">4. 开发运维自动化解决方案</h3>
											<p>从代码到部署运维的一整套自动化方案，通过集成代码托管服务（支持GitHub、BitBucket、GitCafe、Coding等主要代码仓库），构建Docker
												镜像；并一键部署到容器服务平台；动态调整服务的配置、对各个服务进行横向扩展，轻松实现复杂均衡、可自由伸缩的互联网应用框架。</p>

											<h3 id="5-同时支持本地代码构建-docker-镜像">5. 同时支持本地代码构建 Docker 镜像</h3>
											<p>如果您的代码没有托管到GitHub或者BitBucket等代码托管平台上，只有本地的代码或者可部署的应用，BONC-PaaS云平台同样支持从代码到镜像的构建：</p>
											<ol>
												<li>可以支持 Windows、Linux和 Mac 三种平台</li>
												<li>无需关联代码托管服务</li>
												<li>如同使用本地 docker 一样的体验</li>
												<li>不需要打包源代码文件，保证您的代码安全</li>
											</ol>

											<h3 id="6-丰富的镜像服务">6. 丰富的镜像服务</h3>
											<p>我们提供了涵盖云主机、云数据库、Web服务器和博客应用等多样化镜像服务，并进行了系统的分类，满足用户快速上手使用和高级定制化的各类需求。</p>
											<p>我们仍在继续开发更多的镜像服务，以便在服务编排中可以集成更多便利的服务，加速应用开发过程。</p>
											<h3 id="7-更高效的资源利用率，更高的性价比">7. 更高效的资源利用率，更高的性价比</h3>
											<p>与传统的IaaS、PaaS平台相比，价格上我们更有优势；基于镜像的容器服务更加灵活，并可在不同平台上轻松迁移；加上活跃的社区和开发者，
												以及容器技术进一步走向标准化，我们的服务将更丰富、更加标准；随着微服务的推行，我们平台擅长的服务编排、集成的优势也会越来越明显，并带给用户更多优质的服务。</p>

										</section>
									</div>

								</div>


								<div class="page-wrapper tab-pane fade" id="product-info3">
									<div class="page-inner">
										<section class="normal">

											<h1 id="容器服务">容器服务</h1>
											<p>BONC-PaaS的容器服务基于目前异常火热的Docker容器技术，结合Docker生态圈的相关工具及自主研发的方案，提供了一整套的容器集群部署和管理方案。目前提供了诸如云主机、云数据库、Web服务器和博客应用的多样化容器服务，BONC-PaaS云平台能以秒级部署并运行这些容器应用，并支持服务的集群部署、负载均衡、灾难恢复和弹性伸缩。</p>
											<p>本章主要通过以下几个方面来介绍容器服务的主要功能：</p>
											<ul class="info">
												<li>创建一个容器服务</li>
												<li>查看服务信息</li>
												<li>停止/启动/删除容器</li>
												<li>修改服务配置</li>
												<li>弹性伸缩</li>
												<li>灰度升级</li>
											</ul>
										</section>
									</div>
								</div>

								<div class="page-wrapper tab-pane fade" id="product-info3-1">
									<div class="page-inner">
										<section class="normal">
											<h1 id="创建第一个hello-world服务">创建一个容器服务</h1>
											<p>创建一个容器服务只需要下面几个简单步骤：</p>
											<ol>
												<li><p>进入 “容器服务” -&gt; “服务” 页面，点击 “创建” 按钮:</p>
													<p>
														<img src="<%=path%>/images/service-create1.png" alt="create1">
													</p></li>
												<li><p>在新的页面中选择镜像，可以通过“搜索”功能筛选镜像，并点击“部署”按钮开始创建该镜像的容器服务项目。</p>
													<p>
														<img src="<%=path%>/images/service-create2.png" alt="create2">
													</p></li>
												<li><p>创建服务的第二步配置容器资源，有几点需要注意：</p>
													<p>
														<img src="<%=path%>/images/service-create3.png" alt="create3">
														<img src="<%=path%>/images/service-create3.1.png" alt="create3.1">
													</p></li>
												<li>
												<p>（1）带红星的为必填项；</p>
												<p>（2）启动命令：勾选复选框，在文本框中输入启动服务的命令行；</p>
												<p>（3）检查状态:勾选复选框，在文本框内填写应用的一个访问路径；
												需要设置的参数解释：
												检测延迟：此时间参照服务启动所需时间，即为检测生效的时间。例如，应用启动需要5分钟，则设置600s,即6分钟后开始检测；
												检测超时：即访问等待时间。例如：等待时间设置为5秒，则5秒内访问失败则说明应用挂了；
												检测频率：即多久检测一次。</p>
												<p><img src="<%=path%>/images/service-create3.2.png" alt="create3.2">
													</p>
												<p>（4）服务访问路径：填写内容必须和上传的项目名称一致。</p>
												<p>（5）nginx代理区域：DMZ区表示核心区（公网可访问），USER区表示用户区（本地网络可访问）。</p>
												<p>（6）nginx代理路径：填写服务代理路径，建议填写用户名+项目名。</p>
												<p>（7）ClientIp黏连方式：ClientIP即通过容器内部IP(kubernetes service对应的IP)访问黏连。例如设置ClientIP黏连后，通过内部地址访问服务，每次都访问相同的pod。</p>
												<p>（8）NodeIp黏连方式：nodeIPAffinity 即通过外部IP地址访问该应用时，设置NodeIP黏连。例如某应用有多个pod，某个应用通过外部地址(kubernetes node的IP或nginx代理后的IP 加端口)短时间内多次访问该应用，每次都访问相同的pod。</p>
												<p>（9）实例个数：服务启动的pod数量，用于负载均衡</p>
												<p>（10）服务类型：默认为无状态服务，勾选为有状态服务，需要填写挂载地址和选择存储卷</p>
												<p>存储卷需要在服务>存储与备份中创建，如下：</p>
												<p><img src="<%=path%>/images/storage01.png" alt="create3.3"></p>
												<p>填写存储卷名称和存储大小，选择读写权限</p>
												<p><img src="<%=path%>/images/storage02.png" alt="create3.3"></p>
												<p>点击名称查看存储卷的详细信息，有新建文件夹，上传文件，下载文件，删除，解压zip文件功能</p>
												<p><img src="<%=path%>/images/storage03-detail.png" alt="create3.3"></p>
												<p>（11）环境变量：服务启动中的配置参数，环境变量的优先级比配置文件高，当环境变量中没有配置的参数才会读取配置文件中的。</p>
												<p>（12）容器端口：默认配置的是镜像暴露的端口和对应映射到node上的端口，也可手动增加。</p>
												<p><img src="<%=path%>/images/service-create3.3.png" alt="create3.3"></p>
												
													</li>
												<li><p>此时会跳转回到容器服务列表，可以看到列表中新增一条“未启动”的容器服务，点击启动按钮，提示服务启动成功后，运行状态更新为运行中。</p>
													<p>
														<img src="<%=path%>/images/service-create4.png" alt="create4">
													</p></li>
											</ol>
										</section>
									</div>
								</div>
								<div class="page-wrapper tab-pane fade" id="product-info3-2">
									<div class="page-inner">
										<section class="normal">
											<h1 id="查看服务信息">查看服务信息</h1>
											<p>在服务列表页，点击容器名称“centos”，将进入服务详情页面：</p>

											<p>
												<img src="<%=path%>/images/service-detail1.png" alt="service">
											</p>
											<p>在服务详情页面中，可以看到“centos”的详细信息，如“基本信息、容器实例、环境变量、端口、日志”，点击“打开应用”，即可访问服务页面。</p>
											<p>
												<img src="<%=path%>/images/service-detail2.png" alt="service">
											</p>
											<p>容器实例：当前是一个实例</p>
											<p>
												<img src="<%=path%>/images/service-detail3.png" alt="service">
											</p>
											<p>环境变量：环境变量是服务的配置信息，当启动服务时优先读取环境变量的配置信息，当环境变量中没有的配置信息才读取配置文件中的。</p>
											<p>
												<img src="<%=path%>/images/service-detail4.png" alt="service">
											</p>
											<p>端口：</p>
											<p>
												<img src="<%=path%>/images/service-detail5.png" alt="service">
											</p>
											<p>日志：有日志下载到本地功能。</p>
											<p>
												<img src="<%=path%>/images/service-detail6.png" alt="service">
											</p>
										</section>
									</div>
								</div>
								<div class="page-wrapper tab-pane fade" id="product-info3-3">
									<div class="page-inner">
										<section class="normal">
											<h1 id="停止启动删除容器">停止/启动/删除容器</h1>
											<p>
												在服务列表页，勾选服务名称前的复选框，可以对服务进行批量的 <strong>停止<img src="<%=path%>/images/service-stopBtn.png" alt="service">
												/启动<img src="<%=path%>/images/service-startBtn.png" alt="service">
												/删除<img src="<%=path%>/images/service-delBtn.png" alt="service"></strong> 操作：
											</p>
											<p>
												<img src="<%=path%>/images/service-info1.png" alt="service">
											</p>
											<p>服务列表中操作一栏中的功能键都是针对相应服务的一对一功能键，蓝色为当前可操作功能，灰色为当前不可操作的功能键。</p>
											<p>启动容器服务：点击<img src="<%=path%>/images/service-startBtn.png" alt="service">，服务启动成功会有提示框弹出。</p>
											<p><img src="<%=path%>/images/service-startOk.png" alt="service"></p>
											
											<p>停止和删除容启动操作类似。</p>
											
											<h4 id="注：">注：</h4>
											<ul>
												<li><strong>停止/启动/删除</strong>均可以批量操作，但是<strong>停止/启动</strong>操作对容器状态有要求，容器状态不能与操作相同，正在初始化的容器则不能进行<strong>停止/启动</strong>操作</li>
												<li>如果您的容器是操作系统（ubuntu、debian、centos），<strong>停止</strong>后数据会丢失
												</li>
											</ul>
										</section>
									</div>
								</div>
								<div class="page-wrapper tab-pane fade" id="product-info3-4">
									<div class="page-inner">
										<section class="normal">
											<h1 id="更改服务配置">更改服务配置</h1>
											<p>点击<img src="<%=path%>/images/service-peizhiBtn.png" alt="service">更改配置按钮，填写需要更改的CPU和内存，弹出框还显示当前可用的CPU和内存资源：</p>
											<p>
												<img src="<%=path%>/images/service-conf1.png" alt="service">
											</p>
											<p>修改成功后会有弹出提示框。</p>
										</section>
									</div>
								</div>

								<div class="page-wrapper tab-pane fade" id="product-info3-5">
									<div class="page-inner">
										<section class="normal">

											<h1 id="弹性伸缩">弹性伸缩</h1>
											<p>
												点击<img src="<%=path%>/images/service-shensuoBtn.png" alt="scale_up"> <strong>弹性伸缩</strong> 容器实例数量：
											</p>
											<p>在弹出框中填写实例数量，并点击“保存”按钮：</p>
											<p>
												<img src="<%=path%>/images/service-scale1.png" alt="scale_up">
											</p>
											<p>弹性伸缩成功会弹出提示框：</p>
											<p>
												<img src="<%=path%>/images/service-scaleOk.png" alt="scale_up">
											</p>
											<p>此时将返回容器服务列表页，展开之前扩容的服务，可以看到实例数量改变了：</p>
											<p>
												<img src="<%=path%>/images/service-scale2.png" alt="scale_up">
											</p>
										</section>
									</div>
								</div>
								<div class="page-wrapper tab-pane fade" id="product-info3-6">
									<div class="page-inner">
										<section class="normal">

											<h1 id="版本升级">版本升级</h1>
											<p>版本升级可以对已创建的容器服务进行镜像版本升级。使用的镜像需要有多个版本才能进行版本升级。</p>
											<ol>
												<li><p>点击<img src="<%=path%>/images/service-shengjiBtn.png" alt="updata1">版本升级，在下拉选框中选择一个版本.</p>
													<p>
														<img src="<%=path%>/images/service-update1.png" alt="updata1">
													</p></li>
												<li><p>点击“确定”完成版本升级,升级成功会弹出提示框。</p>
												</li>

											</ol>


										</section>
									</div>
								</div>

								<div class="page-wrapper tab-pane fade" id="product-info4">
									<div class="page-inner">
										<section class="normal">

											<h1 id="构建镜像">构建镜像</h1>
											<p>代码构建是一种软件开发实践，在软件开发过程中每天都会发生很多次集成。如果每次集成都能通过自动化的构建（包括编译、打包、自动化测试、发布）来验证，从而通过自动化系统尽快地发现集成错误，会大大减轻开发运维团队的工作，提高软件开发的效率。</p>
											<p>
												<img src="<%=path%>/images/ci1.png" alt="ci">
											</p>
											<p>本章主要介绍构建镜像的四种方法：</p>
											<p>功能键如图所示：<img src="<%=path%>/images/ci-ciBtn.png" alt="ciBtn"></p>
											<ul class="info">
												<li>代码构建</li>
												<li>快速构建</li>
												<li>上传镜像</li>
												<li>dockerfile构建</li>
											</ul>
										</section>
									</div>
								</div>
								<div class="page-wrapper tab-pane fade" id="product-info4-1">
									<div class="page-inner">
										<section class="normal">
											<h1 id="代码构建">代码构建</h1>
											<ol>
												<li>
													<p>待更新...</p>
												</li>
											</ol>
										</section>
									</div>
								</div>
								<div class="page-wrapper tab-pane fade" id="product-info4-2">
									<div class="page-inner">
										<section class="normal">

											<h1 id="快速构建">快速构建</h1>

											<ol>
												<li>
													<p>快速构建方法如下:</p>
													<p>点击下图中红框内快速构建按钮：</p>
													<p><img src="<%=path%>/images/ci-quick1.png" alt="ci"></p>
												</li>
												<li>
												<p>弹出如下页面：</p>
												<p><img src="<%=path%>/images/ci-quick2.png" alt="ci"></p>
												<p>选择构建镜像所需要的基础镜像的名称和版本号，填写镜像名称及版本号，填写新创建的镜像简介，上传制作镜像时需要的代码文件，填写项目名称，选择镜像性质后，点击创建，即可完成创建。示例如下图：</p>
												<p><img src="<%=path%>/images/ci-quick3.png" alt="ci"></p>
												<p>创建完成后，点击下图红框中的按钮构建镜像:</p>
												<p><img src="<%=path%>/images/ci-quick4.png" alt="ci"></p>
												<p>点击红框中按钮后，构建状态显示为构建中:</p>
												<p><img src="<%=path%>/images/ci-quick5.png" alt="ci"></p>
												<p>构建过程中点击项目名称可跳转到如下页面，查看构建日志，项目描述，基本设置，以及删除镜像等操作:</p>
												<p><img src="<%=path%>/images/ci-quick6.png" alt="ci"></p>
												</li>
											</ol>
										</section>
									</div>
								</div>
								<div class="page-wrapper tab-pane fade" id="product-info4-3">
									<div class="page-inner">
										<section class="normal">

											<h1 id="上传镜像">上传镜像</h1>

											<ol>
												<li>
													<p>上传镜像方法如下：</p>
													<p>点击下图中红框内上传镜像按钮：</p>
													<p><img src="<%=path%>/images/ci-uploadimage1.png" alt="ci"></p>
													<p>弹出如下页面：</p>
													<p><img src="<%=path%>/images/ci-uploadimage2.png" alt="ci"></p>
													<p>填写镜像名称和版本号，要求所填写的镜像名称和版本号需与上传镜像中的信息一致，上传镜像文件，选择镜像类型，填写项目名称后，点击构建即可完成创建。示例如下图：</p>
													<p><img src="<%=path%>/images/ci-uploadimage3.png" alt="ci"></p>
													<p>创建完成后，镜像可在镜像中心中查看到：</p>
													<p><img src="<%=path%>/images/ci-uploadimage4.png" alt="ci"></p>
												</li>
											</ol>
										</section>
									</div>
								</div>
								<div class="page-wrapper tab-pane fade" id="product-info4-4">
									<div class="page-inner">
										<section class="normal">

											<h1 id="dockerfile构建">dockerfile构建</h1>

											<ol>
												<li>
													<p>Dockerfile构建方法如下：</p>
													<p>点击下图中红框内Dockerfile构建按钮：</p>
													<p><img src="<%=path%>/images/ci-dockerfile1.png" alt="ci"></p>
													<p>弹出如下页面：</p>
													<p><img src="<%=path%>/images/ci-dockerfile2.png" alt="ci"></p>
													<p>填写镜像名称及版本号，填写简介，上传制作镜像需要的代码文件，选择导入模板或者自行编写Dockerfile,编写的Dockerfile文件可另存为模板，选择创建镜像公有/私有性质，点击创建即可。</p>
													<p>如果需要导入Dockerfile模板，可点击导入模板，选择合适的Dockerfile模板，点击导入按钮即可。导入模板示例如下：</p>
													<p><img src="<%=path%>/images/ci-dockerfile3.png" alt="ci"></p>
													<p>写好的Dockerfile可点击另存为模板，即可生成新的Dockerfile模板，以供后期方便导入使用，Dockerfile编写完成之后点击另存为模板，在弹出框中输入模板名称，点击保存按钮即可。另存为模板示例如下：</p>
													<p><img src="<%=path%>/images/ci-dockerfile4.png" alt="ci"></p>
													<p>Dockerfile创建镜像示例如下：</p>
													<p><img src="<%=path%>/images/ci-dockerfile5.png" alt="ci"></p>
													<p>创建成功后，点击构建即可:</p>
													<p><img src="<%=path%>/images/ci-dockerfile6.png" alt="ci"></p>
												</li>
											</ol>
										</section>
									</div>
								</div>

								<div class="page-wrapper tab-pane fade" id="product-info5">
									<div class="page-inner">
										<section class="normal">

											<h1 id="镜像服务">镜像服务</h1>
											<p>上面我们大概了解了“容器服务”和“持续集成”的主要功能，而且都涉及到了“镜像”的概念，所以上面的功能都离不开“镜像服务”的支持，同样我们也开通了镜像服务板块。容器应用通过镜像服务所提供的各种镜像创建出来，而持续集成最终会生成镜像，并上传到镜像服务器，供大家下载或创建容器服务。</p>
											<p>
												<img src="<%=path%>/images/image1.png" alt="image">
											</p>
											<p>镜像服务分为三个部分：</p>
											<ul class="info">
												<li>浏览镜像</li>
												<li>收藏镜像</li>
												
											</ul>
										</section>
									</div>
								</div>
								<div class="page-wrapper tab-pane fade" id="product-info5-1">
									<div class="page-inner">
										<section class="normal">

											<h1 id="浏览镜像">浏览镜像</h1>
											<p>创建的所有镜像都将保存在镜像中心，可以通过“查找”功能来筛选所需的镜像，直接进行
											部署<img src="<%=path%>/images/image-bushuBtn.png" alt="image">
											/导出<img src="<%=path%>/images/image-exportBtn.png" alt="image">
											/收藏<img src="<%=path%>/images/image-shoucangBtn.png" alt="image">
											/删除<img src="<%=path%>/images/service-delBtn.png" alt="image">操作。</p>
											<p>
												<img src="<%=path%>/images/image1.png" alt="image">
											</p>
											<p>
												点击镜像名称进入镜像详情页面，可以查看镜像的基本信息和版本，进行部署和导出操作，若是该用户构建的镜像还可以进行删除镜像操作。
											</p>
											<p>
												<img src="<%=path%>/images/image-detail.png" alt="image">
											</p>
										</section>
									</div>
								</div>
								<div class="page-wrapper tab-pane fade" id="product-info5-2">
									<div class="page-inner">
										<section class="normal">

											<h1 id="收藏镜像">收藏镜像</h1>
											<p>点击<img src="<%=path%>/images/image-shoucangBtn.png" alt="image">收藏按钮，即可用把镜像文件保存我我的收藏中，以便经常使用。</p>
											<p>
												<img src="<%=path%>/images/image-my.png" alt="image">
											</p>
											<p>该租户构建的镜像都保存在“我的镜像”中，可以进行批量删除操作，收藏的镜像在“我的收藏”列表中。</p>
										</section>
									</div>
								</div>

								<div class="page-wrapper tab-pane fade" id="product-info6">
									<div class="page-inner">
										<section class="normal">
											<h1 id="租户管理">租户管理</h1>
											<p>在租户管理总，云平台管理员可以对租户进行创建、修改、查看、删除等操作。</p>
											<p><img src="<%=path%>/images/user1.png" alt="user"></p>
											<ul class="info">
												<li>创建租户</li>
												<li>查看/修改/删除租户</li>
											</ul>
										</section>
									</div>
								</div>
								<div class="page-wrapper tab-pane fade" id="product-info6-1">
									<div class="page-inner">
										<section class="normal">

											<h1 id="创建租户">创建租户</h1>
											<p>创建租户的步骤如下：</p>
											<p>进入 “租户管理” -&gt; “租户” 页面，点击 “创建” 按钮:</p>
											<p>
												<img src="<%=path%>/images/user-create1.png" alt="user">
											</p>
											<p>填写基本信息，带红星的为必填项，当填写的信息不符合填写规则时，会弹出提示框，</p>
											<p>
												<img src="<%=path%>/images/user-create6.png">
											</p>
											<p>填写完符合规则的信息后，点击“保存”按钮，完成用户的创建。</p>
											<p>页面跳转回租户列表界面，列表中新增一条租户信息。</p>
											

										</section>
									</div>
								</div>
								<div class="page-wrapper tab-pane fade" id="product-info6-2">
									<div class="page-inner">
										<section class="normal">

											<h1 id="修改租户信息">查看/修改/删除租户</h1>
											<p>将鼠标移至“登录账号”名称上方，会出现提示框“查看租户信息”，点击进入租户信息页：</p>
											<p>
												<img src="<%=path%>/images/user-info1.png">
											</p>
											<p>在租户信息页也可以进行租户信息的修改，如图所示，输入框显示为灰色背景的信息项是不能被修改的信息，
												输入框显示为白色背景的信息项为可以修改的信息。点击“下一步”进入资源配置信息的修改。</p>
											<p>
												<img src="<%=path%>/images/user-info2.png">
											</p>
											<p>当前租户查看自己的信息，并可以修改部分信息。</p>
											<p>
												<img src="<%=path%>/images/user-info3.png">
											</p>
											<p>当前租户查看自己的资源使用情况，服务详情中显示当前租户所创建的服务个数和实例个数，而上线均未限制；资源使用情况显示当前租户的资源总量和资源使用量。</p>
											<p>
												<img src="<%=path%>/images/user-info4.png">
											</p>
											<p>当前租户可以修改自己的登录密码。</p>
											<p>
												<img src="<%=path%>/images/user-info5.png">
											</p>
											<h4>注：上述为租户创建用户和查看自己详细信息的操作，若是管理员，则可创建租户并为租户分配资源。</h4> 
											
											

										</section>
									</div>
								</div>


								<div class="page-wrapper tab-pane fade" id="product-info7">
									<div class="page-inner">
										<section class="normal">

											<h1 id="集群管理">集群管理</h1>
											<p>集群管理与实时监控是BONC-PaaS云平台一个特色功能，我们希望通过自身平台的技术和优势为用户提供集群的部署、管理和监控的一整套解决方案。通过集群化的管理用户自己的实体主机、虚拟机或者云主机上的资源，合理规划和充分利用现有的计算和存储资源，并在自己的私有集群上尝试、运用镜像和容器技术，通过这些新技术推进自己的产品在开发、运维、部署、交付等各个环节上的变革及创新，逐渐搭建和形成自己的私有云架构。</p>
											<p>
												<img src="<%=path%>/images/jiqun.png" alt="cluster">
											</p>
											<p>本章主要介绍镜像服务的主要功能：</p>
											<ul class="info">
												<li>集群监控</li>
												<li>容器监控</li>
												<li>集群管理-创建集群节点</li>
												<li>集群拓扑结构</li>
											</ul>
										</section>
									</div>
								</div>

								<div class="page-wrapper tab-pane fade" id="product-info7-1">
									<div class="page-inner">
										<section class="normal">

											<h1 id="集群监控">集群监控</h1>
											<p>集群监控可以查看整个集群的资源使用情况，例如memory、cpu、disk、network使用情况。</p>
											<p>
												<img src="<%=path%>/images/cluster-jiankong.png" alt="cluster">
											</p>
											<p>
												<img src="<%=path%>/images/cluster-jiankong1.png" alt="cluster">
											</p>
											<p>
												<img src="<%=path%>/images/cluster-jiankong2.png" alt="cluster">
											</p>
											<h4>监控图说明：</h4>
											<ul>
												<li><p>
														<strong>Limit</strong>-为可使用的最大资源
													</p></li>
												<li><p>
														<strong>Usage</strong>-为已经使用的资源
													</p></li>
												<li><p>
														<strong>WorkingSet</strong>-为系统或容器运行自身占用的少部分资源
													</p></li>
												<li><p>
														<strong>当memory、CPU和disk的Usage使用资源超出阈值时，usage曲线将出现红色告警信息，曲线上方将显示资源使用情况的数据。</strong>
													</p></li>
											</ul>

										</section>
									</div>
								</div>

								<div class="page-wrapper tab-pane fade" id="product-info7-2">
									<div class="page-inner">
										<section class="normal">

											<h1 id="容器监控">容器监控</h1>
											<p>监控各容器的memory和CPU使用情况，可以通过租户和实例来筛选容器监控范围，默认为全部容器，还可以选定查看的时间范围，默认值为最近5分钟。</p>
											<p>
												<img src="<%=path%>/images/container-jiankong1.png" alt="cluster">
											</p>

											<h4>注：</h4>
											<ul>
												<li><p>
														<strong>当memory和CPU的Usage使用资源超出阈值时，usage曲线将出现红色告警信息，曲线上方将显示资源使用情况的数据。</strong>
													</p></li>
											</ul>
										</section>
									</div>
								</div>

								<div class="page-wrapper tab-pane fade" id="product-info7-3">
									<div class="page-inner">
										<section class="normal">

											<h1 id="创建集群节点">集群管理-创建集群节点</h1>
											<p>进入集群管理界面，点击“创建”按钮：</p>
											<p>
												<img src="<%=path%>/images/cluster-create1.png" alt="cluster">
											</p>
											<p>在输入框内输入查询节点的IP或者IP范围，点击“查找”按钮：</p>
											<p>
												<img src="<%=path%>/images/cluster-create2.png" alt="cluster">
											</p>
											<p>在查询的IP范围内，查询出可以部署的机器，勾选需要安装集群节点IP的复选框，点击“下一步”：</p>
											<p>
												<img src="<%=path%>/images/cluster-create3.png" alt="cluster">
											</p>
											<p>输入ssh登录密码，点击“下一步”：</p>
											<p>
												<img src="<%=path%>/images/cluster-create4.png" alt="cluster">
											</p>
											<p>配置集群节点为master或slave，点击“安装”：</p>
											<p>
												<img src="<%=path%>/images/cluster-create5.png" alt="cluster">
											</p>
											<p>进入集群节点安装界面，进度条和安装状态为“安装进行中。。。”，当安装成功后状态为“安装成功”，若失败则显示“安装失败”：</p>
											<p>
												<img src="<%=path%>/images/cluster-create6.png" alt="cluster">
											</p>
										</section>
									</div>
								</div>
								
								<div class="page-wrapper tab-pane fade" id="product-info7-4">
									<div class="page-inner">
										<section class="normal">

											<h1 id="创建集群节点">集群拓扑</h1>
											<p>进入集群拓扑界面：</p>
											<p>
												<img src="<%=path%>/images/cluster-topo1.png" alt="cluster">
											</p>
											<p>标签从左到右分别表示，master主节点、node从节点、pod容器、service服务。每个标签都可以点击，展示不同维度的集群拓扑关系。</p>
											<p><img src="<%=path%>/images/cluster-topo3.png" alt="cluster"></p>
											<p>可以通过服务筛选所需要查看的集群拓扑关系。</p>
											<p>
												<img src="<%=path%>/images/cluster-topo2.png" alt="cluster">
											</p>
											
										</section>
									</div>
								</div>

								<div class="page-wrapper tab-pane fade" id="product-info8">
									<div class="page-inner">
										<section class="normal">

											<h1 id="常见问题">常见问题</h1>
											<p>常见问题汇聚用户针对BONC-PaaS云平台经常提到的问题，包括新用户入门问题、在使用中遇到的问题等等，我们将这些常见问题整理归纳，希望能快速帮助用户答疑解惑。</p>
											<p>常见问题包含以下几个方面：</p>
											<ul class="info">
												<li>常见问题</li>
												<li>如何编写Dockerfile</li>
											</ul>
										</section>
									</div>
								</div>
								<div class="page-wrapper tab-pane fade" id="product-info8-1">
									<div class="page-inner">
										<section class="normal">

											<h1 id="常见问题">常见问题</h1>

											<h4 id="权限问题">1.管理员、租户、用户的权限区别是什么？</h4>
											<p><img src="<%=path%>/images/authority.png"></p>
											
										</section>
									</div>
								</div>
								<div class="page-wrapper tab-pane fade" id="product-info8-2">
									<div class="page-inner">
										<section class="normal">

											<h1 id="如何编写-dockerfile">如何编写 Dockerfile</h1>
											<h3 id="如何使用-dockerfile">如何使用 Dockerfile</h3>
											<p>Dockerfile
												用来创建一个自定义的image，包含了用户指定的软件依赖等。当前目录下包含Dockerfile，使用命令build来创建新的image，并命名为tenxcloud/helloworld。</p>
											<pre>
												<code>
docker build -t tenxcloud/helloworld .
												</code>
											</pre>
											<h3 id="dockerfile-的关键字">Dockerfile 的关键字</h3>
											<p>如何编写Dockerfile，格式如下：</p>
											<pre>
												<code>
# Comment
INSTRUCTION arguments
													
FROM
基于哪个base镜像
													
RUN
安装软件或者运行命令用
													
MAINTAINER
镜像创建者
													
CMD
container启动时执行的命令，但是一个Dockerfile中只能有一条CMD命令，多条则只执行最后一条CMD。CMD主要用于container时启动指定的服务，当docker run command的命令匹配到CMD command时，会替换CMD执行的命令。
													
ENTRYPOINT
container启动时执行的命令，但是一个Dockerfile中只能有一条ENTRYPOINT命令，如果多条，则只执行最后一条。ENTRYPOINT没有CMD的可替换特性
													
USER
使用哪个用户跑container
													
EXPOSE
													
container内部服务开启的端口。主机上要用还得在启动container时，做host-container的端口映射：
docker run -d -p 127.0.0.1:3000:22 ubuntu-ssh
container ssh服务的22端口被映射到主机的33301端口
													
ENV
用来设置环境变量，比如：ENV ROOT_PASS tenxcloud
													
ADD
将文件&lt;src&gt;拷贝到container的文件系统对应的路径&lt;dest&gt;。ADD只有在build镜像的时候运行一次，后面运行container的时候不会再重新加载了。
													
VOLUME
可以将本地文件夹或者其他container的文件夹挂载到container中。
													
WORKDIR
切换目录用，可以多次切换(相当于cd命令)，对RUN、CMD、ENTRYPOINT生效
													
ONBUILD
ONBUILD 指定的命令在构建镜像时并不执行，而是在它的子镜像中执行
												</code>
											</pre>
											<p>
												更详细的资料，请参考Docker官方关于 <a
													href="https://docs.docker.com/engine/reference/builder"
													target="_blank">Dockerfile</a> 的描述。
											</p>
											<h3 id="如何构建及部署镜像">如何构建及部署镜像</h3>
											<p>
												编写好了Dockerfile之后，您可以通过BONC-PaaS平台构建镜像。具体可以参考 <a
													href="../ci/index.html">代码构建</a>。
											</p>
											<h3 id="一个完整的-dockerfile-的例子">一个完整的 Dockerfile 的例子</h3>
											<pre>
												<code>
# Dockerfile to create a docker image
# Base image
FROM golang:1.4.2
														
# Add project folder to the image
ADD . $GOPATH/src
														
ADD run.sh /run.sh
														
RUN chmod +x /run.sh
														
# Expose the container port
EXPOSE 8080
														
WORKDIR $GOPATH/src
														
CMD ["/run.sh"]
												</code>
											</pre>

										</section>
									</div>
								</div>



							</div>


						</div>
						<!-- book-body -->



					</div>
				</div>
				<!-- row book -->
			</div>
			<!-- container -->
		</article>
	</div>

	<script type="text/javascript">
		$(document).on('click', 'a', function(event) {
			//alert(this.attr('href'))
		})
		//缩放
		$( ".book-summary" ).resizable({
	  		autoHide: true,
			containment: ".book-row",
			maxWidth:470,
			minWidth:50,
			resize: function( event, ui ) {
				var sumWidth = $(".book-summary").width();
				var totalWidth = $(".book-row").width();
				var bodyWidth = totalWidth - sumWidth -40;
				$(".book-body").width(bodyWidth+"px");
			}
		});
		
		$("#a1").click(function() {
			for (var i = 0; i < 9; i++) {
				$("#a" + i + "child").slideUp();
			}
		});
		$("#a2").click(function() {
			$("#a2child").slideDown();
			for (var i = 0; i < 9; i++) {
				if (i != 2) {
					$("#a" + i + "child").slideUp();
				}
			}
			$("#product-info2-0").addClass("active in");
			$("#product-info2-1").removeClass("active in");
			$("#product-info2-2").removeClass("active in");
			$("#product-info2-3").removeClass("active in");
			$(".chapter").removeClass("active");
		});
		$("#a3").click(function() {
			$("#a3child").slideDown();
			for (var i = 0; i < 9; i++) {
				if (i != 3) {
					$("#a" + i + "child").slideUp();
				}
			}
			$("#product-info3").addClass("active in");
			$("#product-info3-1").removeClass("active in");
			$("#product-info3-2").removeClass("active in");
			$("#product-info3-3").removeClass("active in");
			$("#product-info3-4").removeClass("active in");
			$("#product-info3-5").removeClass("active in");
			$("#product-info3-6").removeClass("active in");
			$(".chapter").removeClass("active");
		});
		$("#a4").click(function() {
			$("#a4child").slideDown();
			for (var i = 0; i < 9; i++) {
				if (i != 4) {
					$("#a" + i + "child").slideUp();
				}
			}
			$("#product-info4").addClass("active in");
			$("#product-info4-1").removeClass("active in");
			$("#product-info4-2").removeClass("active in");
			$("#product-info4-3").removeClass("active in");
			$("#product-info4-4").removeClass("active in");
			$(".chapter").removeClass("active");
		});
		$("#a5").click(function() {
			$("#a5child").slideDown();
			for (var i = 0; i < 9; i++) {
				if (i != 5) {
					$("#a" + i + "child").slideUp();
				}
			}
			$("#product-info5").addClass("active in");
			$("#product-info5-1").removeClass("active in");
			$("#product-info5-2").removeClass("active in");
			$(".chapter").removeClass("active");
		});
		$("#a6").click(function() {
			$("#a6child").slideDown();
			for (var i = 0; i < 9; i++) {
				if (i != 6) {
					$("#a" + i + "child").slideUp();
				}
			}
			$("#product-info6").addClass("active in");
			$("#product-info6-1").removeClass("active in");
			$("#product-info6-2").removeClass("active in");
			$(".chapter").removeClass("active");
		});
		$("#a7").click(function() {
			$("#a7child").slideDown();
			for (var i = 0; i < 9; i++) {
				if (i != 7) {
					$("#a" + i + "child").slideUp();
				}
			}
			$("#product-info7").addClass("active in");
			$("#product-info7-1").removeClass("active in");
			$("#product-info7-2").removeClass("active in");
			$("#product-info7-3").removeClass("active in");
			$("#product-info7-4").removeClass("active in");
			$(".chapter").removeClass("active");
		});
		$("#a8").click(function() {
			$("#a8child").slideDown();
			for (var i = 0; i < 9; i++) {
				if (i != 8) {
					$("#a" + i + "child").slideUp();
				}
			}
			$("#product-info8").addClass("active in");
			$("#product-info8-1").removeClass("active in");
			$("#product-info8-2").removeClass("active in");
			$(".chapter").removeClass("active");
		});

		
	</script>
</body>
</html>