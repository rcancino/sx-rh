<table  class="grid table table-striped table-bordered table-condensed incentivoGrid ">
	<thead>
		<tr>
			<th>Folio</th>
			<th>Empleado</th>
			<th>Ubicación</th>
			<th>Faltas</th>
			<th>Incapacidades</th>
			<th>Inicio (A)</th>
			<th>Fin (A)</th>
			<th>Modificado</th> 
		</tr>
	</thead>
	<tbody>
		<g:each in="${asistenciaImssInstanceList}" var="row">
			<tr>
				<td id="${row.id}">
					<g:link action="show" id="${row.id}">
						<g:formatNumber number="${row.id}" format="####"/>
					</g:link>
				</td>
				<td>${fieldValue(bean:row,field:"empleado")}</td>
				<td>${fieldValue(bean:row,field:"empleado.perfil.ubicacion.clave")}</td>
				<td><g:formatNumber number="${row.asistencia.faltas }" format="####"/></td>
				<td><g:formatNumber number="${row.asistencia.incapacidades }" format="####"/></td>
				<td>${g.formatDate(date:row.inicioAsistencia,format:'dd/MM/yyyy')}</td>
				<td>${g.formatDate(date:row.finAsistencia,format:'dd/MM/yyyy')}</td>
				<td><g:formatDate date="${row.lastUpdated}" format="dd/MM/yyyy  HH:mm"/></td>
				
			</tr>
		</g:each>
	</tbody>
</table>