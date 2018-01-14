<%@ page import="com.luxsoft.sw4.rh.Nomina" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Nomina (${nominaInstance.id})</title>
</head>
<body>
	
	<g:set var="pendientesDeTimbrado" 
		value="${nominaInstance.partidas.count{ (it?.cfdi?.uuid == null) && it.total>0.0}}" scope="request" />
	
	<div class="row wrapper border-bottom white-bg page-heading">
	    <div class="col-lg-10">
	    	<g:link action="index" id="${nominaInstance.id}" params="[periodicidad:nominaInstance.periodicidad]">
	    		<h2>Nómina: ${nominaInstance.folio} ${nominaInstance.periodicidad}  
	    			<small> ${nominaInstance.formaDePago }  (${nominaInstance.periodo}) ${nominaInstance.tipo} (${nominaInstance.status})</small>
	    		</h2>
	    		%{-- <g:if test= "${nominaInstance.status == 'CERRADA'}">
	    			
	    		</g:if> --}%
	    	</g:link>
	    	<g:if test="${nominaInstance.status == 'CERRADA'}">
	    		<small><span class='label label-warning' >NOMINA CERRADA</small>
	    		<g:if test ="${nominaInstance.partidas.count{it?.cfdi?.uuid == null} > 0 }">
	    			Con ${pendientesDeTimbrado} registros pendientes de timbrado
	    		</g:if>
	    	</g:if>

	        <g:if test="${flash.message}">
	            <small><span class="label label-warning ">${flash.message}</span></small>
	        </g:if> 
	    </div>
	</div>
	
	<div class=" row wrapper wrapper-content  white-bg animated fadeInRight">
		
		<div class="col-md-12">
			<div class="btn-group">
				<g:link controller="nomina" action="show" id="${nominaInstance.id}" class="btn btn-default btn-outline">
					<span class="glyphicon glyphicon-repeat"></span> Refrescar
				</g:link>
			</div>
			<div class="btn-group">
				<button type="button" name="reportes" class="btn btn-default btn-outline dropdown-toggle" data-toggle="dropdown" role="menu">Reportes <span class="caret"></span></button>
				<ul class="dropdown-menu">
					<li>
						<g:jasperReport
          						jasper="NominaCaratula"
          						format="PDF"
          						name="Carátula">
    							<g:hiddenField name="NOMINA" value="${nominaInstance.id}"/>
 						</g:jasperReport>
					</li>
					
					<g:if test="${['QUINCENAL','SEMANAL'].contains(nominaInstance.periodicidad) }">
						
						
						<li>
							<g:jasperReport
          						jasper="${nominaInstance.periodicidad=='QUINCENAL'?'NominaCaratulaQ':'NominaCaratulaS' }"
          						format="PDF"
          						name="Resumen">
    							<g:hiddenField name="NOMINA" value="${nominaInstance.id}"/>
 							 </g:jasperReport>
						</li>
						
					</g:if>
						<li>
							<g:jasperReport
          						jasper="EmpleadosConPremioDePuntualidad"
          						format="PDF"
          						name="ConPremioDePuntualidad">
    							<g:hiddenField name="ID" value="${nominaInstance.id}"/>
 							 </g:jasperReport>
						</li>		
					<li>
						<button class="btn btn-default btn-outline" data-toggle="modal" data-target="#detalleDeNominaForm"> Detalle</button>
					</li>
				</ul>
			</div> <!-- Fin .btn-group -->
						
			<div class="btn-group">
				<button type="button" name="reportes" class="btn btn-default btn-outline dropdown-toggle" data-toggle="dropdown" >Operaciones <span class="caret"></span></button>
				<ul class="dropdown-menu">
					
					<li>
						<g:link  controller="nominaPorEmpleado" action="create" id="${nominaInstance.id}"> 
							<span class="glyphicon glyphicon-plus"></span> Agregar
						</g:link> 
					</li>
					
					<li>
						<g:link  action="actualizarPartidas" id="${nominaInstance.id}"> 
							<span class="glyphicon glyphicon-cog"></span> Actualizar
						</g:link> 
					</li>
					<li>
						<g:link action="delete" id="${nominaInstance.id}" onclick="return confirm('Eliminar toda la nomina?');"> 
							<span class="glyphicon glyphicon-trash"></span> Eliminar
						</g:link> 
					</li>
					<li>
						<g:link action="generarCfdis" id="${nominaInstance.id}" onclick="return confirm('Generar cfdi's (XML)  toda la nomina?');"> 
							 Generar CFDI's
						</g:link> 
					</li>
					<li>
						<g:link action="timbrar" id="${nominaInstance.id}" onclick="return confirm('Timbrar toda la nomina?');"> 
							 Timbrar CFDI's
						</g:link> 
					</li>
					<li>
						<g:link action="depurar" 
							id="${nominaInstance.id}" 
							onclick="return confirm('Depurar toda la nomina?');"> Depurar
						</g:link> 
					</li>
					<li>
						<g:link  action="ajusteMensualIsr" 
							onClick="return confirm('Aplicar ajuste mensual ISR?');"
							id="${nominaInstance.id}" >
							 Ajuste mensual ISR 
						</g:link>
						
						<g:link  action="eliminarAjusteMensualIsr" 
							onClick="return confirm('Eliminar ajuste mensual ISR?');"
							id="${nominaInstance.id}" >
							 Eliminar ajuste mensual ISR 
						</g:link>
					</li>
					<li>
						<g:link  action="aplicarCalculoAnual" 
							onClick="return confirm('Aplicar calculo anual ISR?');"
							id="${nominaInstance.id}" >
							 Calculo anual
						</g:link>
					</li>
					<li>
						<g:link  action="actualizarSaldos" 
							onClick="return confirm('Actualizar saldos de entidades relacionadas?');"
							id="${nominaInstance.id}" >
							 Actualizar saldos
						</g:link>
					</li>
					<li>
						<g:link  action="aplicarCalculoAnualConSaldo" 
							onClick="return confirm('Aplicar calculo anual ISR con saldo pendiente de aplicar?');"
							id="${nominaInstance.id}" >
							Aplicación de  calculo anual con saldo pendiente
						</g:link>
					</li>
					<li>
						<g:link  controller="reciboDeNomina"
							action="imprimirCfdis" 
							onClick="return confirm('Imprimir todos los recibos de nómina?');"
							id="${nominaInstance.id}" >
							<span class="glyphicon glyphicon-print"></span>
							 Imprimir recibos
						</g:link>
					</li>
					
					
				</ul>
			</div> <!-- Fin .btn-group -->
		</div>

		<div class="col-md-12">
			<ul class="nav nav-tabs" role="tablist">
			   <g:if test="${session.empresa.rfc == 'PAP830101CR3' || session.empresa.rfc == 'OAG100209GN8'}">
	     			  <li class="${tipo=='SEMANA'?'active':''}">
	  				  	<a href="#andrade" role="tab" data-toggle="tab">Andrade</a>
	  				  </li>
	  				  <li><a href="#bolivar" role="tab" data-toggle="tab">Bolivar</a></li>
	  				  <li><a href="#calle4" role="tab" data-toggle="tab">Calle 4</a></li>
	  				  <li><a href="#cf5febrero" role="tab" data-toggle="tab">5 de Febrero</a></li>
	  				  <li><a href="#tacuba" role="tab" data-toggle="tab">Tacuba</a></li>
	  				  
	  				  <li><a href="#ventas" role="tab" data-toggle="tab">Ventas</a></li>
				</g:if>
				<g:if test="${session.empresa.rfc == 'PBA0511077F9' || session.empresa.rfc == 'PAP830101CR3'  || session.empresa.rfc == 'OAG100209GN8'  }">
					<li class="${tipo=='QUINCENA'?'active':''}">
	  				  	<a href="#oficinas" role="tab" data-toggle="tab">Oficinas</a>
	  				  </li>
				</g:if>
			    <g:if test="${session.empresa.rfc == 'PBA0511077F9'}">
					<li class="${tipo=='SEMANA'?'active':''}">
						<a href="#leon" role="tab" data-toggle="tab">Leon</a>
					</li>
			  		<li><a href="#queretaro" role="tab" data-toggle="tab">Queretaro</a></li>
				</g:if>
			</ul>
		</div>
		
		<div class="col-md-12">
				<div class="tab-content">
			  		<div class="tab-pane ${nominaInstance.periodicidad=='SEMANAL' && session.empresa.rfc == 'PAP830101CR3' ?'active':''}" id="andrade">
						<g:render template="nominaDetGridPanel" model="['partidasList':partidasMap['ANDRADE']]"/>
			  		</div>
			  		<div class="tab-pane" id="bolivar">
			  			<g:render template="nominaDetGridPanel" model="['partidasList':partidasMap['BOLIVAR']]"/>
			  		</div>
			  		<div class="tab-pane" id="calle4">
			  			<g:render template="nominaDetGridPanel" model="['partidasList':partidasMap['CALLE4']]"/>
			  		</div>
			  		<div class="tab-pane" id="cf5febrero">
			  			<g:render template="nominaDetGridPanel" model="['partidasList':partidasMap['CF5FEBRERO']]"/>
			  		</div>
			  		<div class="tab-pane" id="tacuba">
			  			<g:render template="nominaDetGridPanel" model="['partidasList':partidasMap['TACUBA']]"/>
			  		</div>
			  		<div class="tab-pane ${nominaInstance.periodicidad=='QUINCENAL'?'active':''}" id="oficinas">
			  			<g:render template="nominaDetGridPanel" model="['partidasList':partidasMap['OFICINAS']]"/>
			  		</div>
			  		<div class="tab-pane" id="ventas">
			  			<g:render template="nominaDetGridPanel" model="['partidasList':partidasMap['VENTAS']]"/>
			  		</div>
			  		<div class="tab-pane" id="queretaro">
			  			<g:render template="nominaDetGridPanel" model="['partidasList':partidasMap['QUERETARO']]"/>
			  		</div>
			  		<div class="tab-pane ${nominaInstance.periodicidad=='SEMANAL' && session.empresa.rfc == 'PBA0511077F9'?'active':''}" id="leon">
			  			<g:render template="nominaDetGridPanel" model="['partidasList':partidasMap['LEON']]"/>
			  		</div>
				</div>
		</div>
	
	</div>

	<g:render template="reporteDetalleDeNominaDialog"/>
	
	<content tag="grid">

		
	</content>



</body>
</html>
