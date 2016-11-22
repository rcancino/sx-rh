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
        <lx:warningLabel/>
        <g:if test="${flash.error}">
            <small><span class="label label-danger ">${flash.error}</span></small>
        </g:if> 
    </div>
</div>

<div class=" row wrapper wrapper-content  white-bg animated fadeInRight">
    
     <div class="row toolbar">
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

<script type="text/javascript">
    $(function(){
        var res = $('.grid').dataTable({
            responsive: true,
            aLengthMenu: [[20, 40, 60, 100, -1], [20, 40,60, 100, "Todos"]],
            "language": {
                "url": "${assetPath(src: 'datatables/dataTables.spanish.txt')}"
            },
            "dom": 't<"clear">lfrip',
            "tableTools": {
                "sSwfPath": "${assetPath(src: 'plugins/dataTables/swf/copy_csv_xls_pdf.swf')}"
            },
            "order": []
        });
        
        var tables = $('.grid'); //.DataTable();

        $("#nombreField").on('keyup',function(e){
            var term=$(this).val();
            $.each(tables, function(p,v) {
                //console.log('Pro: ' + p);
                //console.log('Val: ' + v);
                $(this).DataTable().column(1).search(term).draw();
                
            });
        });
        $("#ubicacionField").on('keyup',function(e){
            var term=$(this).val();
            $.each(tables, function(p,v) {
                $(this).DataTable().column(2).search(term).draw();
                
            });
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