<table  class=" grid table table-striped table-bordered table-condensed">
	<thead>
		<tr>
			<g:sortableColumn property="id" title="Folio"/>
			<g:sortableColumn property="empleado.apellidoPaterno" title="Empleado"/>
			<th>Ubicacion</th>
			<th>Periodicidad</th>
			<th>F. Inicial</th>
			<th>F. Final</th>
			<th>Tipo</th>
			<th>CG</th>
			<th>Comentario</th>
			
			
		</tr>
	</thead>
	<tbody>
		<g:each in="${incidenciaList}" var="row">
			<tr>
				<td>
					<g:link action="show" id="${row.id}">
						<g:formatNumber number="${row.id}" format="####"/>
					</g:link>
				</td>
				<td>
					<g:link action="show" id="${row.id}">
						<g:fieldValue bean="${row}" field="empleado"/>
					</g:link>
				</td>
				<td>${fieldValue(bean:row,field:"empleado.perfil.ubicacion.clave")}</td>
				<td><g:fieldValue bean="${row}" field="empleado.salario.periodicidad"/> </td>
				<td><g:formatDate date="${row.fechaInicial}" format="dd/MM/yyyy"/></td>
				<td><g:formatDate date="${row.fechaFinal}" format="dd/MM/yyyy"/></td>
				<td><g:fieldValue bean="${row}" field="tipo"/> </td>
				<td><g:checkBox name="pagado" value="${row.pagado }" readonly="true" disabled="true"/></td>
				<td><g:fieldValue bean="${row}" field="comentario"/> </td>
				
				
			</tr>
		</g:each>
	</tbody>
</table>
	
