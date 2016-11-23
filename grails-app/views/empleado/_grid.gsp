<table class=" grid table table-striped table-bordered table-condensed">
	<thead>
		<tr>
			<th>Clave</th>
			<th>Nombre</th>
			<th>Ubicación</th>
			<th>Status</th>
			<th>Alta</th>
			<th>Puesto</th>
			<th>Departamento</th>
		</tr>
	</thead>
	<tbody>
		<g:each in="${empleadoInstanceList}" var="row">
			<tr>
				<td>
					<g:link action="show" id="${row.id}">
						${fieldValue(bean:row,field:"clave")}
					</g:link>
				</td>
				<td>
					<g:link action="show" id="${row.id}">
						${fieldValue(bean:row,field:"nombre")}
					</g:link>
				</td>
				<td>${fieldValue(bean:row,field:"perfil.ubicacion.clave")}</td>
				<td>${fieldValue(bean:row,field:"status")}</td>
				<td><g:formatDate date="${row.alta}"/></td>
				<td>${fieldValue(bean:row,field:"perfil.puesto.clave")}</td>
				<td>${fieldValue(bean:row,field:"perfil.departamento.clave")}</td>
				
			</tr>
		</g:each>
	</tbody>
</table>
