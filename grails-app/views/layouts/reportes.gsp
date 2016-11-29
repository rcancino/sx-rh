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
    
    <script type="text/javascript">
        $(function(){
            $('.date').bootstrapDP({
                format: 'dd/mm/yyyy',
                keyboardNavigation: false,
                forceParse: false,
                autoclose: true,
                todayHighlight: true
            });
            $('.chosen-select').chosen();
            $(".numeric").autoNumeric('init',{vMin:'0'},{vMax:'9999'});
            $(".money").autoNumeric('init',{wEmpty:'zero',mRound:'B',aSign: '$'});
            $(".tc").autoNumeric('init',{vMin:'0.0000'});
            $(".porcentaje").autoNumeric('init',{altDec: '%', vMax: '99.99'});

            
        });
    </script> 
        
        
    </body>
</g:applyLayout>