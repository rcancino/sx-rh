<%@ page import="com.luxsoft.sw4.rh.Incentivo" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="operaciones2"/>
	<title>Asistencia (IMSS)</title>
	
</head>
<body>

<content tag="header">
	<h2>Registro de asistencia IMSS 
	<small><a href="" data-toggle="modal" data-target="#calendarioForm">
			${calendarioDet.calendario.tipo}  ${calendarioDet.folio } (${session.ejercicio })
		</a></small></h2>
        <ol class="breadcrumb">
    	    <li >
    	    	<g:if test="${calendarioDet.calendario.tipo=='QUINCENA'}">
    	    		<strong>Quincenal</strong>
    	    	</g:if>
    	    	<g:else>
    	    		<g:link action="index" params="[tipo:'QUINCENA'	]" >
    	    			Quincenal
    	    		</g:link>
    	    	</g:else>
    	    	
    	    </li>
    	    <li >
    	    	<g:if test="${calendarioDet.calendario.tipo =='SEMANA'}">
    	    		<strong>Semanal</strong>
    	    	</g:if>
    	    	<g:else>
    	    		<g:link action="index" params="[tipo:'SEMANA'	]" >
    	    			Semanal	
    	    		</g:link>
    	    		
    	    	</g:else>
    	    	
    	    </li>
		</ol>
</content>
	
<content tag="buttonsBar">
	<g:link action="index" params="[tipo:calendarioDet.calendario.tipo]"
			class="btn btn-default btn-outline">
			<i class="fa fa-refresh"></i> Refrescar
	</g:link>
	<g:link action="generar" 
			class="btn btn-warning btn-outline" 
			onclick="return confirm('Generar/Actualizar asistencia IMSS: '+'${calendarioDet.calendario.tipo} ${calendarioDet.folio}');" 
			id="${calendarioDet.id}">
			<i class="fa fa-cog"></i> Generar
	</g:link>
	<g:link action="deleteAll" class="btn btn-danger " id="${calendarioDet.id}"
			onclick="return confirm('Eliminar registros de asistencia IMSS: '+'${calendarioDet.calendario.tipo} ${calendarioDet.folio}');">
	    <i class="fa fa-trash"></i> Eliminar todo
	</g:link> 
</content>
<content tag="operacionesPanel">
	
</content>
<content tag="reportes">
	<li><a  data-toggle="modal"	data-target="#reporteDeAusentismoSua"> Ausentismo SUA</a></li>
	<li>
		<a  data-toggle="modal"	data-target="#reporteDeAusentismoSuaAsist"> Ausentismo SUA Asistencia	</a>
	</li>
	<li>
		<a  data-toggle="modal"	data-target="#reporteDeComparativoSua"> Comparativo SUA	</a>
	</li>
</content>
	
<content tag="toolbarPanel">
	
	<div class="row button-panel">
		
		<div class="col-md-4 ">
			<input id="searchField" class="form-control" type="text" placeholder="Empleado" autofocus="autofocus">
		</div>

		<div class="col-md-8">
			<div class="btn-group">
				<g:link action="index" params="[tipo:calendarioDet.calendario.tipo]"
						class="btn btn-default">
						<span class="glyphicon glyphicon-refresh"></span> Refresacar
				</g:link>
				<g:link action="generar" 
						class="btn btn-default" 
						onclick="return confirm('Generar/Actualizar asistencia IMSS: '+'${calendarioDet.calendario.tipo} ${calendarioDet.folio}');" 
						id="${calendarioDet.id}">
						<span class="glyphicon glyphicon-cog"></span> Generar
				</g:link>
				<g:link action="deleteAll" class="btn btn-default " id="${calendarioDet.id}"
						onclick="return confirm('Eliminar registros de asistencia IMSS: '+'${calendarioDet.calendario.tipo} ${calendarioDet.folio}');" 
					>
				    <i class="fa fa-trash"></i> Eliminar todo
				</g:link> 
			</div>
			<div class="btn-group">
				<button type="button" name="reportes"
					class="btn btn-default dropdown-toggle" data-toggle="dropdown"
					role="menu">
					Reportes <span class="caret"></span>
				</button>
				<ul class="dropdown-menu">
					<li>
						<a  data-toggle="modal"	data-target="#reporteDeAusentismoSua"> Ausentismo SUA	</a>
					</li>
					<li>
						<a  data-toggle="modal"	data-target="#reporteDeAusentismoSuaAsist"> Ausentismo SUA Asistencia	</a>
					</li>
					<li>
						<a  data-toggle="modal"	data-target="#reporteDeComparativoSua"> Comparativo SUA	</a>
					</li>
				</ul>
			</div><%-- end .btn-group reportes --%>
		</div><%-- end .col-md button panel --%>
		
	</div>	
	
</content>
	
<content tag="gridPanel">
	
	<g:render template="gridPanel"/>
	<g:render template="selectorDeCalendarioDialog"/>
	<g:render template="reporteDeAusentismoSua"/>
	<g:render template="reporteDeComparativoSua"/>
	<g:render template="reporteDeAusentismoSuaAsist"/>
</content>

</body>
</html>