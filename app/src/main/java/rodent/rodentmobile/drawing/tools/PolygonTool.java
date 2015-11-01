package rodent.rodentmobile.drawing.tools;

import rodent.rodentmobile.drawing.shapes.PolygonShape;

/**
 * Created by Atte on 25.10.2015.
 */
public class PolygonTool extends PolyLineTool {

    public PolygonTool () {
        super();
        this.setShape(new PolygonShape());
    }

}
