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

package org.flowable.engine.test.bpmn.java;

import java.io.ByteArrayInputStream;

import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.FlowableListener;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.EndEvent;
import org.flowable.bpmn.model.ImplementationType;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.SequenceFlow;
import org.flowable.bpmn.model.StartEvent;
import org.flowable.bpmn.model.TimerEventDefinition;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.common.impl.util.io.InputStreamSource;
import org.flowable.engine.impl.test.PluggableFlowableTestCase;
import org.flowable.engine.repository.Deployment;

/**
 * @author Tijs Rademakers
 */
public class EventJavaTest extends PluggableFlowableTestCase {

  public void testStartEventWithExecutionListener() throws Exception {
    BpmnModel bpmnModel = new BpmnModel();
    Process process = new Process();
    process.setId("simpleProcess");
    process.setName("Very simple process");
    bpmnModel.getProcesses().add(process);
    StartEvent startEvent = new StartEvent();
    startEvent.setId("startEvent1");
    TimerEventDefinition timerDef = new TimerEventDefinition();
    timerDef.setTimeDuration("PT5M");
    startEvent.getEventDefinitions().add(timerDef);
    FlowableListener listener = new FlowableListener();
    listener.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_EXPRESSION);
    listener.setImplementation("${test}");
    listener.setEvent("end");
    startEvent.getExecutionListeners().add(listener);
    process.addFlowElement(startEvent);
    UserTask task = new UserTask();
    task.setId("reviewTask");
    task.setAssignee("kermit");
    process.addFlowElement(task);
    SequenceFlow flow1 = new SequenceFlow();
    flow1.setId("flow1");
    flow1.setSourceRef("startEvent1");
    flow1.setTargetRef("reviewTask");
    process.addFlowElement(flow1);
    EndEvent endEvent = new EndEvent();
    endEvent.setId("endEvent1");
    process.addFlowElement(endEvent);

    byte[] xml = new BpmnXMLConverter().convertToXML(bpmnModel);

    new BpmnXMLConverter().validateModel(new InputStreamSource(new ByteArrayInputStream(xml)));

    Deployment deployment = repositoryService.createDeployment().name("test").addString("test.bpmn20.xml", new String(xml)).deploy();
    repositoryService.deleteDeployment(deployment.getId());
  }
}
