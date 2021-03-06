/*
 * IronJacamar, a Java EE Connector Architecture implementation
 * Copyright 2015, Red Hat Inc, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the Eclipse Public License 1.0 as
 * published by the Free Software Foundation.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the Eclipse
 * Public License for more details.
 *
 * You should have received a copy of the Eclipse Public License 
 * along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.ironjacamar.rars.test.inflow;

import org.ironjacamar.rars.test.TestResourceAdapter;

import java.lang.reflect.Method;

import javax.resource.ResourceException;
import javax.resource.spi.endpoint.MessageEndpoint;
import javax.resource.spi.endpoint.MessageEndpointFactory;

/**
 * TestActivation
 */
public class TestActivation
{
   /** The resource adapter */
   private TestResourceAdapter ra;

   /** Activation spec */
   private TestActivationSpec spec;

   /** The message endpoint factory */
   private MessageEndpointFactory endpointFactory;

   /**
    * Default constructor
    * @exception ResourceException Thrown if an error occurs
    */
   public TestActivation() throws ResourceException
   {
      this(null, null, null);
   }

   /**
    * Constructor
    * @param ra TestResourceAdapter
    * @param endpointFactory MessageEndpointFactory
    * @param spec TestActivationSpec
    * @exception ResourceException Thrown if an error occurs
    */
   public TestActivation(TestResourceAdapter ra, 
                         MessageEndpointFactory endpointFactory,
                         TestActivationSpec spec) throws ResourceException

   {
      this.ra = ra;
      this.endpointFactory = endpointFactory;
      this.spec = spec;
   }

   /**
    * Get activation spec class
    * @return Activation spec
    */
   public TestActivationSpec getActivationSpec()
   {
      return spec;
   }

   /**
    * Get message endpoint factory
    * @return Message endpoint factory
    */
   public MessageEndpointFactory getMessageEndpointFactory()
   {
      return endpointFactory;
   }

   /**
    * Start the activation
    * @throws ResourceException Thrown if an error occurs
    */
   public void start() throws ResourceException
   {
      try
      {
         Method m = TestMessageListener.class.getMethod("onMessage", new Class<?>[] {String.class});
         
         MessageEndpoint me = endpointFactory.createEndpoint(null);
         me.beforeDelivery(m);

         TestMessageListener tml = (TestMessageListener)me;
         tml.onMessage(spec.getName());
         
         me.afterDelivery();
         me.release();
      }
      catch (ResourceException re)
      {
         throw re;
      }
      catch (Exception e)
      {
         throw new ResourceException(e);
      }
   }

   /**
    * Stop the activation
    */
   public void stop()
   {

   }
}
