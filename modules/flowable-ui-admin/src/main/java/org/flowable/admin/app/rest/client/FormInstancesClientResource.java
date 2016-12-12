/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.flowable.admin.app.rest.client;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.flowable.admin.domain.EndpointType;
import org.flowable.admin.domain.ServerConfig;
import org.flowable.admin.service.engine.FormInstanceService;
import org.flowable.admin.service.engine.exception.FlowableServiceException;
import org.flowable.app.service.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author Bassam Al-Sarori
 * @author Yvo Swillens
 */
@RestController
public class FormInstancesClientResource extends AbstractClientResource {

  @Autowired
  protected FormInstanceService clientService;

  @Autowired
  protected ObjectMapper objectMapper;

  @RequestMapping(value = "/rest/activiti/form-definition-form-instances/{formDefinitionId}", method = RequestMethod.GET, produces = "application/json")
  public JsonNode listFormSubmittedForms(HttpServletRequest request, @PathVariable String formDefinitionId) {
    ServerConfig serverConfig = retrieveServerConfig(EndpointType.FORM);

    try {
      ObjectNode bodyNode = objectMapper.createObjectNode();
      bodyNode.put("formDefinitionId", formDefinitionId);

      return clientService.getFormInstances(serverConfig, bodyNode);
    } catch (FlowableServiceException e) {
      throw new BadRequestException(e.getMessage());
    }
  }

  @RequestMapping(value = "/rest/activiti/process-form-instances/{processInstanceId}", method = RequestMethod.GET, produces = "application/json")
  public JsonNode getProcessSubmittedForms(@PathVariable String processInstanceId) {
    ServerConfig serverConfig = retrieveServerConfig(EndpointType.FORM);

    try {
      ObjectNode bodyNode = objectMapper.createObjectNode();
      bodyNode.put("processInstanceId", processInstanceId);

      return clientService.getFormInstances(serverConfig, bodyNode);
    } catch (FlowableServiceException e) {
      throw new BadRequestException(e.getMessage());
    }
  }

  @RequestMapping(value = "/rest/activiti/form-instances", method = RequestMethod.GET, produces = "application/json")
  public JsonNode getSubmittedForm(HttpServletRequest request) {
    ServerConfig serverConfig = retrieveServerConfig(EndpointType.FORM);

    Map<String, String[]> parameterMap = getRequestParametersWithoutServerId(request);

    return clientService.listFormInstances(serverConfig, parameterMap);
  }
}
