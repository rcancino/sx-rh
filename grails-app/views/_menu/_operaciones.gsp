<g:set var="operacionesControllers" 
    value="${['nomina','operacionGenerica','incentivo','prestamo']}" />
<li class="${operacionesControllers.contains(webRequest.controllerName)?'active':''}">
    <a href="#">
    	<i class="fa fa-tasks"></i> 
    	<span class="nav-label"> Operaciones</span>
    	<span class="fa arrow"></span>
    </a>
    <ul class="nav nav-second-level collapse">
        
        <li class="${webRequest.controllerName=='nomina'?'active':''}">
            <g:link controller="nomina">
                <span class="nav-label">Nómina</span> 
                <span class="fa fa-angle-right pull-right"></span>
            </g:link>
            <span class="fa fa-angle-right pull-right"></span>
        </li>

        <li class="${webRequest.controllerName=='operacionGenerica'?'active':''}">
            <g:link controller="operacionGenerica">
                <span class="nav-label">Genéricas</span> 
                <spzxan class="fa fa-angle-right pull-right"></span>
            </g:link>
            <span class="fa fa-angle-right pull-right"></span>
        </li>

        <li class="${webRequest.controllerName=='reciboDeNomina'?'active':''}">
            <g:link controller="reciboDeNomina">
                <span class="nav-label">Recibos (Quincena)</span> 
                <span class="fa fa-angle-right pull-right"></span>
            </g:link>
            <span class="fa fa-angle-right pull-right"></span>
        </li>

        <li class="${webRequest.controllerName=='reciboDeNomina'?'active':''}">
            <g:link controller="reciboDeNomina" action="semanal">
                <span class="nav-label">Recibos (Semana)</span> 
                <span class="fa fa-angle-right pull-right"></span>
            </g:link>
            <span class="fa fa-angle-right pull-right"></span>
        </li>

        <li class="${webRequest.controllerName=='incentivo'?'active':''}">
            <g:link controller="incentivo" action="semanal">
                <span class="nav-label">Incentivo</span> 
                <span class="fa fa-angle-right pull-right"></span>
            </g:link>
            <span class="fa fa-angle-right pull-right"></span>
        </li>

        <li class="${webRequest.controllerName=='prestamo'?'active':''}">
            <g:link controller="prestamo" >
                <span class="nav-label">Prestamos</span> 
                <span class="fa fa-angle-right pull-right"></span>
            </g:link>
            <span class="fa fa-angle-right pull-right"></span>
        </li>
        
    </ul>
</li>