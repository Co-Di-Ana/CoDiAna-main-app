package cz.edu.x3m.controls;

import java.util.List;

/**
 *
 * @author Jan Hybs <x3mSpeedy@gmail.com>
 */
public interface IController {
    
    List<UpdateResult> update () throws Exception;
}
