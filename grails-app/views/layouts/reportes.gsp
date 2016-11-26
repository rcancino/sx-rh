<g:applyLayout name="application">
    <html>
    <head>
        <title><g:layoutTitle/></title>
        <g:layoutHead/>
    </head>
    </html>
    <body>
        
        <lx:header>
            <h3>Módulo de reportes</h3>
            <lx:warningLabel/>
        </lx:header>

        <div class=" row wrapper wrapper-content  white-bg animated fadeInRight">
            <div class="col-md-3">

                <nav:menu id="reportesTaskPanel" custom="true" scope="app/reportes">
                    <li class="${item.data.icon ? 'i_'+item.data.icon : ''}">
                            
                            <div class="panel-group" id="accordion">
                                <div class="panel panel-default">
                                    <div class="panel-heading">
                                        <h4 class="panel-title">
                                            <a data-toggle="collapse" data-parent="#accordion"
                                                href="#collapse${item.titleDefault}"> <nav:title item="${item}"/> </a>
                                        </h4>
                                    </div>
                                    <div id="collapse${item.titleDefault}" class="panel-collapse collapse ">
                                        <g:if test="${item.children}">
                                        <nav:menu class="nav nav-pills nav-stacked" scope="${item.id}" />
                                        
                                         </g:if>
                                    </div>
                                </div>
                            </div>

                           
                        </li>
                </nav:menu>
                
            </div>
            
            <div class="col-md-9">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h4 class="panel-title"><g:pageProperty name="page.reporteTitle"/></h4>
                    </div>
                    <div class="panel-body">
                        <g:pageProperty name="page.reportForm"/>
                    </div>
                </div>
            </div>
        </div>

        
        
    </body>
</g:applyLayout>