<g:set var="reportesControllers" 
    value="${['reportes']}" />
<g:set var="controller" value="${webRequest.controllerName}" />

<li class="${reportesControllers.contains(webRequest.controllerName)?'active':''}">
    <a href="#">
    	<i class="fa fa-line-chart"></i> 
    	<span class="nav-label"> Reportes</span>
    	<span class="fa arrow"></span>
    </a>
    <ul class="nav nav-second-level collapse">
        
        %{-- Reportes de nominas --}%
        <li class="${reportesControllers.contains(controller)}">
            <a href="#">Nomina <span class="fa arrow"></span></a>
            <ul class="nav nav-third-level">

                <li class="">
                    <g:link controller="reporte" action="impuestoSobreNominas">
                        <span class="nav-label">Impuestos sobre nómina</span> 
                    </g:link>
                </li>

                <li class="">
                    <g:link controller="reporte" action="acumuladoDeNominasPorConcepto">
                        <span class="nav-label">Acumulado por concepto</span> 
                    </g:link>
                </li>

                

            </ul>
        </li>
        
    </ul>
</li>