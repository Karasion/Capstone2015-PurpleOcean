/**
 * @file HUDClassTemplet.java
 * @brief This file is associated with a HUD layout class template.
 * @details This file is composed of HUDClassTemplate class.
 */

/**
 * @namespace kr.ac.kookmin.cs.hud
 * @brief This package is a set of classes related to HUD.
 * @details This package is composed of HUD management class
 *          and HUD function class .
 */
package kr.ac.kookmin.cs.hud;

import eu.opends.main.Simulator;

/**
 * @brief This class stub is a template for the HUD layout class.
 * @details All HUD layout class must inherit this class.
 *          And some method must always be implemented.
 * @author Jo-kwanghyeon
 */
public abstract class HUDClassTemplate {
  /**
   * @brief You must initialize the elements of layout in this method.
   *        And you must also add a menu bar icon .
   * @param Simulator argument simulator. This is used to utilize the API in Jme3.
   * @return Nothing
   */
  public abstract void init(Simulator simulator);
  /**
   * @brief Inside this method it is necessary to implement the tasks required when HUD layout inserted.
   * @param Nothing
   * @return Nothing
   */
  public abstract void attach();
  /**
   * @brief Inside this method it is necessary to implement the tasks required when HUD layout deleted.
   * @param Nothing
   * @return Nothing
   */
  public abstract void detach();
  /**
   * @brief This method to implement the layout change .
   * @param Nothing
   * @return Nothing
   */
  public abstract void update();
  //	public abstract void regist();
  /**
   * @brief This metho to implement what you need in the application that HUD is paused.
   * @param Nothing
   * @return Nothing
   */
  public void pause()
  {
  }
  /**
   * @brief This metho to implement what you need in the application that HUD is resumed .
   * @param Nothing
   * @return Nothing
   */
  public void resume()
  {
  }
  /**
   * @brief This metho to implement the action of push key.
   * @param Nothing
   * @return Nothing
   */
  public void key_act_push()
  {
  }
  /**
   * @brief This metho to implement the action of right key.
   * @param Nothing
   * @return Nothing
   */
  public void key_act_right()
  {
  }
  /**
   * @brief This metho to implement the action of left key.
   * @param Nothing
   * @return Nothing
   */
  public void key_act_left()
  {
  }
  /**
   * @brief This metho to implement the action of up key.
   * @param Nothing
   * @return Nothing
   */
  public void key_act_up()
  {
  }
  /**
   * @brief This metho to implement the action of down key.
   * @param Nothing
   * @return Nothing
   */
  public void key_act_down()
  {
  }
}
