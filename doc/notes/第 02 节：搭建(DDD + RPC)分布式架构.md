# 第 02 节：搭建(DDD + RPC)分布式架构

<div align="center">
    <img src="https://codechina.csdn.net/KnowledgePlanet/Lottery/-/raw/master/doc/assets/img/2-00.png" width="128">
</div>

- 分支：[210801_xfg_initProject](https://codechina.csdn.net/KnowledgePlanet/Lottery/-/tree/210801_xfg_initProject)
- 描述：基于DDD架构模型，初始化搭建工程结构

本节是陆续搭建系统和编码的开始，我们会优先完成一个基础工程的创建。一般在互联网企业这部分工作可能不需要反复处理，只需要在承接产品需要后使用脚手架或者直接复制以往工程就可以创建现有需要使用的工程了。例如 Spring 官网也提供了创建工程的脚手架，[https://start.spring.io](https://start.spring.io/) Spring Initializr 本质上也是一个 Web 应用，它可以通过 Web 界面、Spring Tool Suite、IntelliJ IDEA 等方式，构建出一个基本的 Spring Boot 项目结构。**但是**，我们创建的项目结构并是一个简单的 MVC 结构，而是需要基于 DDD 四层架构进行模块化拆分，并把分布式组件 RPC 结合进行，所以这里我们需要进行框架搭建。

## DDD 分层架构介绍

>DDD（Domain-Driven Design 领域驱动设计）是由Eric Evans最先提出，目的是对软件所涉及到的领域进行建模，以应对系统规模过大时引起的软件复杂性的问题。整个过程大概是这样的，开发团队和领域专家一起通过 通用语言(Ubiquitous Language)去理解和消化领域知识，从领域知识中提取和划分为一个一个的子领域（核心子域，通用子域，支撑子域），并在子领域上建立模型，再重复以上步骤，这样周而复始，构建出一套符合当前领域的模型。

![]()






