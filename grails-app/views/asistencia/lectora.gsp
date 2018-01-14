<%@ page import="com.luxsoft.sw4.rh.Checado" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Lectora</title>
</head>
<body>

<lx:header><h2>Registros de lectora</h2>
	<a href="#periodoForm" data-toggle="modal">
			<small>Registros de lectora periodo:  ${periodo}  Registros:${checadoTotalCount}</small>
	</a>
</lx:header>

<div class=" row wrapper wrapper-content  white-bg animated fadeInRight">
	<div class="row toolbar">
	    <div class="col-md-12">
		    <div class="btn-group">
		         <button class="btn btn-info btn-outline" data-toggle="modal" data-target="#importarForm">
		       		<span class="glyphicon glyphicon-import"></span> Importar
		       	</button>
		       	
		       		
	       		%{-- <lx:refreshButton action="lectora"/> --}%
		       		
	     		<g:link class="btn btn-default btn-outline" action="eliminarRegistrosLectora" 
	     		onclick="return confirm('Eliminar registros de checado del periodo ' );">
	     			<span class="glyphicon glyphicon-trash"></span> Eliminar
	     		</g:link>
					
			<div class="btn-group">
				<button type="button" name="reportes"
					class="btn btn-outline btn-default dropdown-toggle" data-toggle="dropdown"
					role="menu">
					Reportes <span class="caret"></span>
				</button>
				<ul class="dropdown-menu">
					
							
				</ul>
				
			</div>



		    </div>	
	    </div>
	</div>
	<g:render template="lecturasGridPanel"/>
</div>
	
<g:render template="/_common/periodoForm" model="[action:'actualizarPeriodoDeLecturas',periodo:periodo]"/>	
%{-- <g:render template="/_common/periodoForm" ,model="[modalId:'importarDialog',action:'importarLecturas']"/>	 --}%
<!-- Modal para el alta de percepciones -->
<div class="modal fade" id="importarForm" tabindex="-1">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="importarDialogForm">
					Importar registros del periodo: ${periodo}
				</h4>
			</div>
			<g:form action="importarLecturas" class="form-horizontal">
				
				<div class="modal-body">
					<div class="form-group">
    					<label for="fechaIni" class="col-sm-3">Fecha inicial</label>
    					<div class="col-sm-9">
    						<input type="text" class="form-control  date" id="fechaIni" name="fechaInicial" 
    							value="${g.formatDate(date:periodo.fechaInicial,format:'dd/MM/yyyy') }" disabled>
    					</div>
  					</div>
  					
  					<div class="form-group">
    					<label for="fechaFin" class="col-sm-3">Fecha final</label>
    					<div class="col-sm-9" >
    						<input type="text" class="form-control date" id="fechaFin" name="fechaFinal" 
    							value="${g.formatDate(date:periodo.fechaFinal,format:'dd/MM/yyyy') }" disabled>
    					</div>
  					</div>
  					
				</div>
				
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
					<g:submitButton class="btn btn-primary" name="Ejecutar"
							value="ejecutar" />
				</div>
				
			</g:form>


		</div>
		<!-- moda-content -->
	</div>
	<!-- modal-di -->
</div>

<script type="text/javascript">
    $(function(){
        var res = $('.grid').dataTable({
            responsive: true,
            aLengthMenu: [[20, 40, 60, 100, -1], [20, 40,60, 100, "Todos"]],
            "language": {
                "url": "${assetPath(src: 'datatables/dataTables.spanish.txt')}"
            },
            "dom": 'lftrip',
            "tableTools": {
                "sSwfPath": "${assetPath(src: 'plugins/dataTables/swf/copy_csv_xls_pdf.swf')}"
            },
            "order": []
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
</html>