<table  class="grid table table-striped table-bordered table-condensed incentivoGrid ">
	<thead>
		<tr>
			<th>Folio</th>
			<th>Empleado</th>
			<th>Ubicación</th>
			<th>Modificado</th> 
		</tr>
	</thead>
	<tbody>
		<g:each in="${finiquitoInstanceList}" var="row">
			<tr>
				<td id="${row.id}">
					<g:link action="show" id="${row.id}">
						<g:formatNumber number="${row.id}" format="####"/>
					</g:link>
				</td>
				<td>${fieldValue(bean:row,field:"empleado")}</td>
				<td>${fieldValue(bean:row,field:"empleado.perfil.ubicacion.clave")}</td>
				<td><g:formatDate date="${row.lastUpdated}" format="dd/MM/yyyy  HH:mm"/></td>
				
			</tr>
		</g:each>
	</tbody>
</table>