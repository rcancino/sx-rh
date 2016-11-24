<div class="ibox float-e-margins">
	<div class="ibox-title"> <h5>Empleados</h5></div>
	
	<div class="ibox-content">

		<table id ="empleadosGrid" class="table">
			<thead>
				<tr>
					<th>Nombre</th>
					<th>Ubicación</th>
					<g:if test="${editable == true}">
						<th></th>
					</g:if>
				</tr>
			</thead>
			<tbody>
				<g:each in="${vacacionesGrupoInstance.partidas}" var="row">
					<tr>
						<td>
							<g:link controller="vacaciones" action="edit" id="${row.id}">
								${row.empleado}	
							</g:link>
							
						</td>
						<td>${row.empleado.perfil.ubicacion.clave}</td>
						
						<g:if test="${editable == true}">
							<td>
								<g:link onclick="confirm('Eliminar vacaciones para el empleado');"
									action="eleiminarVacaciones" id="${vacacionesGrupoInstance.id}" params="[vacacionesId:row.id]">
									<i class="fa fa-trash"></i>
								</g:link>
							</td>
						</g:if>
						
					</tr>
				</g:each>
			</tbody>
		</table>
		
	</div>

	
	<g:if test="${editable == true}">
		<div class="ibox-footer">
			<g:link class="btn btn-primary btn-sm" 
				action="selectorDeEmpleados" 
				id="${vacacionesGrupoInstance.id}">
                <i class="fa fa-plus"></i>  Agregar empleado
			</g:link>
			
		</div>
	</g:if>
	
</div>

<script type="text/javascript">
	$(function(){
		$('#empleadosGrid').dataTable({
		    responsive: true,
		    aLengthMenu: [[20, 40, 60, 100, -1], [20, 40,60, 100, "Todos"]],
		    "language": {
		        "url": "${assetPath(src: 'datatables/dataTables.spanish.txt')}"
		    },
		    "dom": 'lfrtip',
		    "tableTools": {
		        "sSwfPath": "${assetPath(src: 'plugins/dataTables/swf/copy_csv_xls_pdf.swf')}"
		    },
		    "order": []
		});
		var table = $('#grid').DataTable();
		$("#filtro").on('keyup',function(e){
		    var term=$(this).val();
		    table.search(term).draw();
		});
	});
</script>



