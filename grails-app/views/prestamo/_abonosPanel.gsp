<lx:ibox>
	<lx:iboxTitle title="Abonos">
		<lx:iboxTools>
			<g:link action="agregarAbono" id="${prestamoInstance.id}">
				<i class="fa fa-plus"></i> Agregar Abono
			</g:link>
		</lx:iboxTools>
	</lx:iboxTitle>
	<lx:iboxContent>
		<table class="table table-striped table-bordered table-condensed">
			<thead>
				<tr>
					<g:sortableColumn property="id" title="Folio"/>
					<g:sortableColumn property="fecha" title="Fecha"/>
					<th><g:message code="prestamoAbono.importe.label" default="Importe" encodeAs="html"/></th>
					<th><g:message code="prestamoAbono.nomina.label" default="Nomina" encodeAs="html"/></th>
					<th></th>
				</tr>
			</thead>
			<tbody>
				<g:each in="${abonos}" var="row">
					<tr>
						<td><lx:idFormat id="${row.id}"/></td>
						<td><g:formatDate date="${row.fecha}" format="dd/MM/yyyy"/></td>
						<td>
							<g:formatNumber number="${row.importe}" format="#,###.##"/>
						</td>
						<td>
							<g:link controller="nominaPorEmpleado" action="edit" id="${row?.nominaPorEmpleadoDet?.parent?.id}">
								<g:formatNumber number="${row?.nominaPorEmpleadoDet?.parent?.nomina?.folio}" format="######"/>
								(<g:formatNumber number="${row?.nominaPorEmpleadoDet?.parent?.nomina?.ejercicio}" format="####"/>)
								
							</g:link>
						</td>
						<td>	
							<g:link action="eliminarPartida" id="${row.id }" onClick="return confirm('Seguro que desea eliminar el abono?')"><span class="glyphicon glyphicon-trash"></span></g:link>
						</td>
						
						
					</tr>
				</g:each>
			</tbody>
		</table>
	</lx:iboxContent>
</lx:ibox>


