<%@ page import="com.luxsoft.sw4.rh.Incentivo" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Incentivo Semanal</title>
</head>
<body>
	
	<lx:header>
	    <div class="col-lg-10">
	        <h2>Control de incentivos ${tipo}</h2>
	        <a href="" data-toggle="modal" data-target="#calendarioForm">
	        	<strong>${mes} - ${ejercicio}</strong> 
	        </a>
	        	<ol class="breadcrumb">
	        		<li ><g:link action="semanal">Semanal</g:link></li>
            	    <li ><g:link action="quincenal">Quincenal</g:link></li>
            	    <li ><strong>Mensual</strong></li>
				</ol>
	        <lx:warningLabel/>
	    </div>
	</lx:header>
	<div class="row wrapper  white-bg annimated">

		<div class="row toolbar">
			
			<div class="col-md-2 ">
				<input id="filtro" class="form-control" type="text" placeholder="Filtro" autofocus="autofocus">
			</div>
			<div class="col-md-2">
			    <input id="ubicacionField" placeholder="Ubicacion" class="form-control" autocomplete="off" >
			</div>

			<div class="col-md-8">
				<div class="btn-group">
					<lx:refreshButton action="${webRequest.actionName}"/>
					<a href="#generarIncentivoForm" data-toggle="modal" class="btn btn-default btn-outline" >
			 			<span class="glyphicon glyphicon-cog"></span> Generar
					</a>
					
					<a href="#calcularIncentivoMensualForm" data-toggle="modal" class="btn btn-primary btn-outline" >
			 			<span class="glyphicon glyphicon-wrench"></span> Calcular
					</a>
					<a href="#semanaForm" data-toggle="modal" class="btn btn-default btn-outline" >
			 			<span class="glyphicon glyphicon-calendar"></span> Asignar calendario
					</a>
					<div class="btn-group">
						<button type="button" name="reportes"
							class="btn btn-success btn-outline dropdown-toggle" data-toggle="dropdown"
							role="menu">
							Reportes <span class="caret"></span>
						</button>
						<ul class="dropdown-menu">
							<li>
								<a href="#reporteIncentivoMensualDialog" data-toggle="modal">Incentivo</a>
							</li>
						</ul>
					</div><%-- end .btn-group reportes --%>
				</div>
			</div><%-- end .col-md button panel --%>
		</div>

		<div class="row">
			<div class="col-md-12">
				%{-- <g:render template="gridPanel" /> --}%
				<g:render template="gridMensual" />
			</div>
		</div>
	</div>
	
	
	<g:render template="mesesDialog"/>
	<g:render template="generacionMensualDialog" />
	<g:render template="calculoMensualDialog" />
	<g:render template="reporteIncentivoMensualDialog" />
	
	
	
		
		
		
	<div class="modal fade" id="semanaForm" tabindex="-1">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="myModalLabel">Periodo</h4>
				</div>
				<g:form action="asignarCalendarioDeIncentivoMensual" class="form-horizontal">
					
					<g:hiddenField name="ejercicio" value="${ejercicio}"/>
					<g:hiddenField name="mes" value="${mes}"/>
					<div class="modal-body">
						<div class="form-group">
	    					<label for="calendarioIni" class="col-sm-3">Calendario</label>
	    					<div class="col-sm-9">
	    						<g:select id="calendarioField" class="form-control"  
									name="calendarioDetId" 
									value="${calendarioDet}"
									from="${periodos}" 
									optionKey="id" 
									optionValue="${{it.calendario.tipo+' '+it.folio+' ( '+it.inicio.format('yyyy-MMM-dd')+' al '+it.fin.format('yyyy-MMM-dd')+ ' )'+' (Asis: '+it.asistencia.fechaInicial.format('yyyy-MMM-dd')+' al '+it.asistencia.fechaFinal.format('yyyy-MMM-dd')+ ' )'}}"
									/>
									
	    					</div>
	  					</div>
	  					
					</div>
					
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
						<g:submitButton class="btn btn-primary" name="aceptar"
								value="Aceptar" />
					</div>
					
				</g:form>
			</div> <!-- moda-content -->
		</div>
	</div> <!-- modal-di -->
		
	<script type="text/javascript">
	    $(function(){
	        $('#grid').dataTable({
	            responsive: true,
	            aLengthMenu: [[20, 40, 60, 100, -1], [20, 40,60, 100, "Todos"]],
	            "language": {
	                "url": "${assetPath(src: 'datatables/dataTables.spanish.txt')}"
	            },
	            "dom": 't<"clear">lrip',
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

	        $('.porcentaje').autoNumeric({vMin:'0.00',vMax:'99.00',mDec:'6'});

	    });
	</script>  
	

</body>
</html>