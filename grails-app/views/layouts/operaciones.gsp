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
        <g:pageProperty name="page.header"/>
        <g:if test="${flash.message}">
            <small><span class="label label-warning ">${flash.message}</span></small>
        </g:if> 
        <g:if test="${flash.error}">
            <small><span class="label label-danger ">${flash.error}</span></small>
        </g:if> 
    </div>
</div>

<div class=" row wrapper wrapper-content animated fadeInRight">
    
    <div class="col-lg-12">
        <div class="ibox float-e-margins">
            
            <div class="ibox-content">
                <div class="row">
                    <g:if test="${g.pageProperty(name:'page.filtros')}">
                        <g:pageProperty name="page.filtros"/>
                    </g:if>
                    <g:else>
                        <div class="col-md-3">
                            <input id="nombreField" placeholder="Empleado" class="form-control" autofocus="autofocus" autocomplete="off">
                        </div>
                        <div class="col-md-3">
                            <input id="ubicacionField" placeholder="Ubicacion" class="form-control" autocomplete="off" >
                        </div>
                    </g:else>
                    <div class="btn-group">
                        <lx:refreshButton/>
                        <lx:createButton/>
                    </div>
                    <div class="btn-group">
                        <button type="button" name="operaciones"
                                class="btn btn-success btn-outline dropdown-toggle" data-toggle="dropdown"
                                role="menu">
                                Operaciones <span class="caret"></span>
                        </button>
                        <ul class="dropdown-menu">
                            <g:pageProperty name="page.operaciones"/>
                        </ul>
                    </div>
                    <div class="btn-group">
                        <button type="button" name="reportes"
                                class="btn btn-primary btn-outline dropdown-toggle" data-toggle="dropdown"
                                role="menu">
                                Reportes <span class="caret"></span>
                        </button>
                        <ul class="dropdown-menu">
                            <g:pageProperty name="page.reportes"/>
                        </ul>
                    </div>
                </div>
               <g:pageProperty name="page.gridPanel"/>
            </div>
        </div>
    </div>
</div>

        <g:if test="${pageProperty(name:'page.periodoDialog')}">
            <g:pageProperty name="page.periodoDialog"/>
        </g:if>

        <g:else>
            %{-- <g:render template="/common/selectorDePeriodo" model="[periodo:periodo]"/> --}%
        </g:else>
        
        <g:if test="${pageProperty(name:'page.searchPanel')}">
            <g:pageProperty name="page.searchPanel"/>
        </g:if>
        

        <script type="text/javascript">
            $(function(){
                $('#grid').dataTable({
                    responsive: true,
                    aLengthMenu: [[20, 40, 60, 100, -1], [20, 40,60, 100, "Todos"]],
                    "language": {
                        "url": "${assetPath(src: 'datatables/dataTables.spanish.txt')}"
                    },
                    "dom": 'T<"clear">lfrtip',
                    "tableTools": {
                        "sSwfPath": "${assetPath(src: 'plugins/dataTables/swf/copy_csv_xls_pdf.swf')}"
                    },
                    "order": []
                });
                $("#filtro").on('keyup',function(e){
                    var term=$(this).val();
                    $('#grid').DataTable().search(
                        $(this).val()
                            
                    ).draw();
                });

                var table = $('#grid').DataTable();

                $("#nombreField").on('keyup',function(e){
                    var term=$(this).val();
                    table.column(1).search(term).draw();
                });
                $("#ubicacionField").on('keyup',function(e){
                    var term=$(this).val();
                    table.column(2).search(term).draw();
                });

                $('.date').bootstrapDP({
                    format: 'dd/mm/yyyy',
                    keyboardNavigation: false,
                    forceParse: false,
                    autoclose: true,
                    todayHighlight: true,

                });

                

                

            });
        </script>  
        
    </body>
</g:applyLayout>