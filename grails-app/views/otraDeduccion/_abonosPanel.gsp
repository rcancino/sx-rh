<lx:ibox>
	<lx:iboxTitle title="Abonos">
		<lx:iboxTools>
			
		</lx:iboxTools>
	</lx:iboxTitle>
	<lx:iboxContent>
		<table class="table table-striped table-bordered table-condensed">
			<thead>
				<tr>
					<th>Folio</th>
					<th>Fecha</th>
					<th>Importe</th>
					<th>Nómina</th>
					<th>Modificado</th>
					<th>Eliminar</th>
				</tr>
			</thead>
			<tbody>
				<g:each in="${otraDeduccionInstance.abonos}" var="row">
					<tr>
						<td><g:formatNumber number="${row.id}" format="######"/></td>
						<td><g:formatDate date="${row.fecha}" format="dd/MM/yyyy"/></td>
						<td>
							<g:formatNumber number="${row.importe}" format="#,###.##"/>
						</td>
						
						
						<td>
							<g:link controller="nominaPorEmpleado" action="edit" id="${row?.nominaPorEmpleadoDet?.parent?.id}">
								<g:formatNumber number="${row?.nominaPorEmpleadoDet?.parent?.nomina?.folio}" format="######"/>
							</g:link>
						</td>
						<td><g:formatDate date="${row.lastUpdated}" format="dd/MM/yyyy hh:mm"/></td>
						<td>	
							<g:link action="eliminarPartida" id="${row.id }" onClick="return confirm('Seguro que desea eliminar el abono?')"><span class="glyphicon glyphicon-trash"></span></g:link>
						</td>
					</tr>
				</g:each>
			</tbody>
		</table>
	</lx:iboxContent>
	<lx:iboxFooter>
		<g:link class="btn btn-primary btn-outline btn-sm" action="agregarAbono" id="${otraDeduccionInstance.id}">
				<i class="fa fa-plus"></i> Agregar Abono
		</g:link>
	</lx:iboxFooter>
</lx:ibox>




