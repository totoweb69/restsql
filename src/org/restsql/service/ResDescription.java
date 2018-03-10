/* Copyright (c) restSQL Project Contributors. Licensed under MIT. */
package org.restsql.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import org.restsql.core.Factory;
import org.restsql.core.SqlResource;
import org.restsql.service.monitoring.MonitoringFactory;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Timer;
import com.codahale.metrics.annotation.Timed;
import org.apache.commons.lang.StringUtils;
import org.restsql.core.sqlresource.Choix;
import org.restsql.core.sqlresource.PlaceHolder;

/**
 * Contains core JAX-RS Resource of the service, processing SQL Resource CRUD
 * requests. Also lists available resources.
 *
 * @author Mark Sawers
 */
@Path("conf/res/descriptions")
public class ResDescription {


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(
            @Context final UriInfo uriInfo, @HeaderParam("Accept") String acceptMediaType,
            @Context final HttpServletRequest httpRequest, @Context final SecurityContext securityContext) {
        try {
            List<String> names = Factory.getSqlResourceNames();
            StringBuilder retour = new StringBuilder();
            int nb = 0;
            retour.append("[");
            for (String name : names) {
                SqlResource ress = Factory.getSqlResource(name);
                String json = getJson(ress);
                if (StringUtils.isNotEmpty(json)) {
                    if (nb > 0) {
                        retour.append(",");
                    }
                    retour.append(json);
                    nb++;
                }

            }
            retour.append("]");

            return Response.ok(retour.toString()).build();
        } catch (Exception e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }

    }

    @GET
    @Path("{resName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("resName") final String resName,
            @Context final UriInfo uriInfo,
            @HeaderParam("Accept") String acceptMediaType, @Context final HttpServletRequest httpRequest,
            @Context final SecurityContext securityContext) {
        try {
            StringBuilder retour = new StringBuilder();
            SqlResource ress = Factory.getSqlResource(resName);

            if (ress != null) {
                String json = getJson(ress);
                if (StringUtils.isNotEmpty(json)) {
                    retour.append(json);
                    return Response.ok(retour.toString()).build();
                }
            }
            return Response.status(Status.NOT_FOUND).build();
        } catch (Exception e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }

    }

    // Private utils
    String getJson(SqlResource ress) {
        try {
            StringBuilder retour = new StringBuilder();
            retour.append("{");

            //Le nom
            retour.append("\"nom\": \"").append(ress.getName()).append("\",");
            //Le nom d'extraction
            retour.append("\"nomExtraction\": ");
            if (StringUtils.isNotEmpty(ress.getDefinition().getMetadata().getNomExtraction())) {
                retour.append("\"").append(ress.getDefinition().getMetadata().getNomExtraction()).append("\"");
            }else{
                retour.append("null");
            }
            retour.append(", ");
            //Les paramètres
            int nb = 0;
            retour.append("\"parametres\": [");
            for (PlaceHolder pl : ress.getDefinition().getMetadata().getPlaceHolder()) {
                String json = getJson(pl);
                if (StringUtils.isNotEmpty(json)) {
                    if (nb > 0) {
                        retour.append(",");
                    }
                    retour.append(json);
                    nb++;
                }
            }
            retour.append("]");
            retour.append("}");
            return retour.toString();
        } catch (Exception e) {
            return null;
        }
    }

    String getJson(PlaceHolder param) {
        try {
            StringBuilder retour = new StringBuilder();
            retour.append("{ ");
            //Le nom du paramètre
            retour.append("\"nom\": \"").append(StringUtils.strip(param.getName())).append("\", ");
            
            //Le label du paramètre
            if(StringUtils.isNotEmpty(param.getLabel())){
                retour.append("\"label\": \"").append(StringUtils.strip(param.getLabel())).append("\", ");
            }else{
                retour.append("\"label\": null").append(", ");
            }
            
            //Le type
            retour.append("\"type\": \"").append(StringUtils.strip(param.getType())).append("\", ");
            //Le format
            if(StringUtils.isNotEmpty(param.getFormat())){
                retour.append("\"format\": \"").append(StringUtils.strip(param.getFormat())).append("\", ");
            }else{
                retour.append("\"format\": null").append(", ");
            }
            //La valeur par défaut
            if(StringUtils.isNotEmpty(param.getValeurParDefaut())){
                retour.append("\"valeurParDefaut\": \"").append(StringUtils.strip(param.getValeurParDefaut())).append("\", ");
            }else{
                retour.append("\"valeurParDefaut\": null").append(", ");
            }
            //Les choix
            int nb = 0;
            retour.append("\"choix\": [");
            for (Choix choix : param.getChoix()) {
                String json = getJson(choix);
                if (StringUtils.isNotEmpty(json)) {
                    if (nb > 0) {
                        retour.append(",");
                    }
                    retour.append(json);
                    nb++;
                }
            }
            retour.append("]");
            retour.append("}");

            return retour.toString();
        } catch (Exception e) {
            return null;
        }
    }

    String getJson(Choix choix) {
        try {
            StringBuilder retour = new StringBuilder();
            retour.append("{ ");
            //Le nom du choix
            retour.append("\"label\": \"").append(StringUtils.strip(choix.getLabel())).append("\", ");
            //La valeur du choix
            retour.append("\"value\": \"").append(StringUtils.strip(choix.getValue())).append("\" ");
            retour.append("}");
            return retour.toString();
        } catch (Exception e) {
            return null;
        }
    }

}
