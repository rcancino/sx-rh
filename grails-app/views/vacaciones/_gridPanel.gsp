

<table id="vacacionesGrid" class="table table-striped table-bordered table-condensed">
	<thead>
		<tr>
			<g:sortableColumn property="id" title="Folio"/>
			<g:sortableColumn property="empleado.apellidoPaterno" title="Empleado"/>
			<th>Ubicación</th>
			<th>Periodicidad</th>
			<th>Pagados</th>
			<th>Días</th>
			<th>Modificado</th>
		</tr>
	</thead>
	<tbody>
		<g:each in="${vacacionesList}" var="row">
			<tr>
				<td>
					<g:link action="edit" id="${row.id}">
						<g:formatNumber number="${row.id}" format="####"/>
					</g:link>
				</td>
				<td>
					<g:link action="edit" id="${row.id}">
						<g:fieldValue bean="${row}" field="empleado"/>
					</g:link>
				</td>
				<td>${fieldValue(bean:row,field:"empleado.perfil.ubicacion.clave")}</td>
				<td>${fieldValue(bean:row,field:"empleado.salario.periodicidad")}</td>
				<td>${fieldValue(bean:row,field:"comentario")}</td>
				%{-- <td><g:fieldValue bean="${row}" field="pg"/></td> --}%
				<td><g:formatNumber number="${row.pg?row.diasPagados:row.dias.size()}" format="###"/></td>
				<td><g:formatDate date="${row.lastUpdated}" format="dd/MM/yyyy"/></td>
				
			</tr>
		</g:each>
	</tbody>
</table>
<div class="pagination">
	<g:paginate total="${vacacionesTotalCount ?: 0}" />
</div>
