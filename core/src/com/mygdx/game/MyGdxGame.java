package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;
import java.util.List;

public class MyGdxGame extends ApplicationAdapter {
    SpriteBatch batch;
    Texture img;    World world;
    OrthographicCamera camera;
    Box2DDebugRenderer renderer;
    List<Body> bodies = new ArrayList<Body>();

    @Override
    public void create() {
        batch = new SpriteBatch();


        camera = new OrthographicCamera(100, 100);
        camera.position.set(0, 0, 0);

        world = new World(Vector2.Zero, true);

        renderer = new Box2DDebugRenderer();

        Body planet = createCircle(5, 0, 0);
        planet.setType(BodyDef.BodyType.StaticBody);

        float angle = MathUtils.PI;
        Vector2 pos = (new Vector2(MathUtils.sin(angle), MathUtils.cos(angle))).scl(20);
        Body body = createCircle(1.0f, pos.x, pos.y);
        bodies.add(body);


        Vector2 dir = new Vector2();
        dir.set(1.0f, 1.0f);
        dir.scl(64f);
        body.setLinearVelocity(dir);


    }


    private Body createCircle(float r, float x, float y) {
        BodyDef nodeBodyDefinition = new BodyDef();
        nodeBodyDefinition.type = BodyDef.BodyType.DynamicBody;
        Vector2 center = new Vector2();
        nodeBodyDefinition.position.set(10, 10);

        CircleShape shape = new CircleShape();
        float density = 1.0f;
        shape.setRadius(r);

        Body body = world.createBody(nodeBodyDefinition);
        body.setUserData(this);
        body.setTransform(x, y, 0);
        final FixtureDef nodeFixtureDefinition = createFixtureDefinition(shape, density);

        body.createFixture(nodeFixtureDefinition);
        shape.dispose();

        return body;
    }

    private static FixtureDef createFixtureDefinition(final Shape shape, final float density) {
        final FixtureDef nodeFixtureDefinition = new FixtureDef();
        nodeFixtureDefinition.shape = shape;
        nodeFixtureDefinition.friction = 1;
        nodeFixtureDefinition.density = density;
        nodeFixtureDefinition.restitution = 0.1f;
        return nodeFixtureDefinition;
    }


    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        world.step(Gdx.graphics.getDeltaTime(), 4, 4);
        camera.update();

        for (Body body : bodies) {
            Vector2 gravityDirection = (new Vector2()).sub(body.getWorldCenter().x, body.getWorldCenter().y);
            gravityDirection.scl(10);
            body.applyForce(gravityDirection, body.getWorldCenter(), true);
        }

        renderer.render(world, camera.combined);
    }
}
