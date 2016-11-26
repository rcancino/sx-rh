<html>
<head>
	<meta charset="UTF-8">
	<title>Conceptos de nómina</title>
</head>
<body>
	
	<lx:header>
		<h2>Catálogo de conceptos de nómina</h2>
		<lx:warningLabel bean="${conceptoDeNominaInstance}"/>
		<ol class="breadcrumb"	>
			<li>
				<g:if test="${webRequest.actionName == 'percepciones'}"><strong>Precepciones</strong></g:if>
				<g:else><g:link action="percepciones">Percepciones</g:link></g:else>
			</li>
			<li>
				<g:if test="${webRequest.actionName == 'deducciones'}"><strong>Deducciones</strong></g:if>
				<g:else><g:link action="deducciones">Deducciones</g:link></g:else>
			</li>
		</ol>
	</lx:header>
	
	<div class=" row wrapper  animated fadeInRight">
		<div class="row toolbar">
		    
		    <div class="col-md-3">
		        <input id="nombreField" placeholder="Empleado" class="form-control" autofocus="autofocus" autocomplete="off">
		    </div>
		    <div class="col-md-3">
		        <input id="ubicacionField" placeholder="Ubicacion" class="form-control" autocomplete="off" >
		    </div>
		    <div class="btn-group">
		       	<lx:refreshButton/>
		        <lx:createButton/>
		    </div>
		    <div class="btn-group">
		        <button type="button" name="reportes"
		                class="btn btn-primary btn-outline dropdown-toggle" data-toggle="dropdown"
		                role="menu">
		                Reportes <span class="caret"></span>
		        </button>
		        <ul class="dropdown-menu">
		            
		        </ul>
		    </div>
		</div>
		<lx:ibox>
			<lx:iboxContent>
				<g:render template="grid"/>
			</lx:iboxContent>
		</lx:ibox>
	</div>

	<script type="text/javascript">
	    $(function(){
	        var res = $('.grid').dataTable({
	            responsive: true,
	            aLengthMenu: [[20, 40, 60, 100, -1], [20, 40,60, 100, "Todos"]],
	            "language": {
	                "url": "${assetPath(src: 'datatables/dataTables.spanish.txt')}"
	            },
	            "dom": 'lTf<"clear">tfrip',
	            "tableTools": {
	                "sSwfPath": "${assetPath(src: 'plugins/dataTables/swf/copy_csv_xls_pdf.swf')}"
	            },
	            "order": []
	        });
	        
	        var tables = $('.grid'); //.DataTable();

	        $("#filter").on('keyup',function(e){
	            var term=$(this).val();
	            $.each(tables, function(p,v) {
	                $(this).DataTable().column(1).search(term).draw();
	                
	            });
	        });

	    });
	</script>  	
	
</body>
</html>