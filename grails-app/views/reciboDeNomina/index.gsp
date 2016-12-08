<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Recibos de nómina</title>
</head>
<body>

	<div class="row wrapper border-bottom white-bg page-heading">
	    <div class="col-lg-10">
	        <h2> Recibos de nómina
	        	<small>
	        		<a href="" data-toggle="modal" data-target="#cambioGlobalDeEjercicioForm">${session.ejercicio}</a>
				</small>
			</h2>

	        <ol class="breadcrumb">
        	    <li >
        	    	<g:link action="index" params="[periodicidad:'QUINCENAL'	]" >
        	    		<g:if test="${periodicidad=='QUINCENAL'}">
        	    			<strong>Quincenal</strong>
        	    		</g:if>
        	    		<g:else>
        	    			Quincenal
        	    		</g:else>
        	    	</g:link>
        	    </li>
        	    <li >
        	    	<g:link action="index" params="[periodicidad:'SEMANAL'	]" >
        	    		<g:if test="${periodicidad=='SEMANAL'}">
        	    			<strong>Semanal</strong>
        	    		</g:if>
        	    		<g:else>
        	    			Semanal
        	    		</g:else>
        	    	</g:link>
        	    </li>

	        </ol>
	        <g:if test="${flash.message}">
	            <small><span class="label label-warning ">${flash.message}</span></small>
	        </g:if> 
	    </div>
	</div>
	<div class=" row wrapper wrapper-content  white-bg animated fadeInRight">
	
		<div class="col-md-12">
			<div class="button-panel">
				<div class="btn-group">
					<input type='text' id="folioField" placeholder="folio" class="form-control" autocomplete="off" autofocus="autofocus">
				</div>
				<div class="btn-group">
					<input type='text' id="tipoField" placeholder="tipo" class="form-control" autocomplete="off" >
				</div>
				<div class="btn-group">
					<input type='text' id="formaDePagoField" placeholder="fomra de pago" class="form-control" autocomplete="off" >
				</div>

				<div class="btn-group">
					
					<div class="btn-group">
						<button type="button" name="reportes"
							class="btn btn-success btn-outline dropdown-toggle" data-toggle="dropdown"
							role="menu">
							Operaciones <span class="caret"></span>
						</button>
						<ul class="dropdown-menu">
							<li>
								<g:link action="descargar" onclick="return confirm('Descargar todos los recibos de nomina del ejercicio?');"> <i class="fa fa-dwonload"></i> Descargar recibos</g:link>
							</li>
						</ul>
					</div>	
				</div>
			</div>
		</div>
		<div class="col-md-12">
			<g:render template="grid"/>
		</div>
		
		
	</div>

	%{-- <div class="container-fluid">
		<div class="row">
			<div class="col-md-12">
				<div class="page-header">
				<h2>Recibos de nómina <strong>session.ejercicio</strong></h2>
				</div>
			</div>
			
		</div>

		<div class="row">
			<div class="col-md-2">
				<div class="alert alert-info">
					<h4 class="text-center">Nomina</h4>
				</div>
				<div class="panel-group" id="accordion">
					<g:each in="${com.luxsoft.sw4.Mes.meses}" var="mes">
					<div class="panel panel-default">
						
							<div class="panel-heading">
							<h4 class="panel-title">
								<a href="#${mes.nombre}" data-toggle="collapse" data-parent="#accordion">${mes.nombre}</a>
							</h4>
						</div>
						<div class="panell-collapse collapse ${mesInstance==mes.nombre?'in':'' }" id="${mes.nombre}">
							<div class="panel-body">
								<ul class="nav nav-pills nav-stacked">
									<g:each in="${nominasPorMesInstanceMap[mes.nombre]}" var="nomina">
										<li class="${nomina.id==nominaInstance?.id?'active':'' }">
											<g:link  params="[nominaId:nomina.id,mesInstance:mes.nombre]" >Folio: ${nomina.folio} (${nomina.tipo})</g:link>
										</li>
									</g:each>
								</ul>
								
							</div>
						</div>
						
					</div>
					</g:each>
				</div>
			</div>
			<div class="col-md-10">
				<div class="alert alert-info">
					<h4 class="text-center">Recibos</h4>
				</div>
				<g:render template="grid"/>
			</div>
		</div>

	</div> --}%
</body>
</html>