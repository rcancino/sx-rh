<g:applyLayout name="application">
<html>
<head>
    <title><g:layoutTitle/></title>
    <g:layoutHead/>
</head>
</html>
    <body>

        <div class="row wrapper border-bottom white-bg page-heading">
            <div class="col-lg-10">
                <h2>${pageProperty(name:'page.header')?:"Falta header"} </h2>
                
                <g:if test="${flash.message}">
                    <small><span class="label label-warning ">${flash.message}</span></small>
                </g:if> 
                <g:if test="${flash.error}">
                    <small><span class="label label-danger ">${flash.error}</span></small>
                </g:if> 
            </div>
        </div>

        <div class="wrapper wrapper-content animated fadeInRight">
            <div class="row">
                <div class="col-lg-8">
                    <div class="ibox float-e-margins">
                        <div class="ibox-title">
                            <h5>${pageProperty(name:'page.formTitle')?:'Propiedades'} </h5>
                        </div>
                        <form class="form-horizontal" >  
                            <div class="ibox-content">
                                <lx:errorsHeader bean="${entity}"/>
                                <g:pageProperty name="page.form"/>
                            </div>
                            <div class="ibox-footer">
                                <g:if test="${g.pageProperty(name:'page.footer')}">
                                    <g:pageProperty name="page.footer"/>
                                </g:if>
                                <g:else>
                                    <div class="btn-group">
                                        <lx:backButton/>
                                        <lx:createButton/>
                                        <g:if test="${editable}">
                                            <lx:editButton id="${entity?.id}"/>
                                        </g:if>
                                        <g:if test="${printable}">
                                            <lx:printButton id="${entity?.id}"/>
                                        </g:if>
                                    </div>
                                </g:else>
                                
                            </div>
                        </form>
                        
                    </div>
                </div>
            </div>
        </div>
             
    </body>
</g:applyLayout>