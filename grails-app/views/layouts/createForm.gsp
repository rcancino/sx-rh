<g:applyLayout name="application">
    <html>
    <head>
        <title><g:layoutTitle/></title>
        <asset:javascript src="forms/forms.js"/>
        <g:layoutHead/>
    </head>
    </html>
    <body>

        <div class="row wrapper border-bottom white-bg page-heading">
            <div class="col-lg-10">
                <h2>${pageProperty(name:'page.header')?:"Alta de ${entityName}"} </h2>
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
                <div class="col-lg-10 col-lg-offset-1">
                    <div class="ibox float-e-margins">
                        <div class="ibox-title">
                            <h5>${pageProperty(name:'page.formTitle')?:'Propiedades'} </h5>
                        </div>
                        <lx:errorsHeader bean="${entity}"/>
                        <g:form name="createForm" action="save" class="form-horizontal" method="POST">  
                            <div class="ibox-content">
                                <g:pageProperty name="page.formFields"/>
                            </div>
                            <div class="ibox-footer">
                                <div class="btn-group">
                                    <button id="saveBtn" class="btn btn-primary ">
                                        <i class="fa fa-floppy-o"></i> Salvar
                                    </button>
                                    <g:if test="${g.pageProperty(name:'page.backButton')}">
                                        <g:pageProperty name="page.backButton"/>
                                    </g:if>
                                    <g:else>
                                        <lx:backButton id="${entity.id}" />
                                    </g:else>
                                    
                                </div>
                            </div>
                        </g:form>
                    </div>
                </div>
            </div>
        </div>

        <g:if test="${pageProperty(name:'page.js')}">
            <script type="text/javascript">
                ${pageProperty(name:'page.js')}
            </script>
        </g:if>
        <g:else>
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

                    $('form[name=createForm]').submit(function(e){
                        console.log("Desablidatndo submit button....");

                        var button=$("#saveBtn");
                        button.attr('disabled','disabled')
                        .html('Procesando...');

                        $(".money,.porcentaje,.numeric,.tc",this).each(function(index,element){
                          var val=$(element).val();
                          var name=$(this).attr('name');
                          var newVal=$(this).autoNumeric('get');
                          $(this).val(newVal);
                        });
                        //e.preventDefault(); 
                        return true;
                    });

                });
            </script> 
        </g:else>
    

        
        
             
    </body>
</g:applyLayout>