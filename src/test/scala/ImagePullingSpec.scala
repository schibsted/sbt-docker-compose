import com.tapad.docker.DockerComposePlugin.*
import com.tapad.docker.{ DockerComposePluginLocal, ServiceInfo }
import org.mockito.Mockito.*
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.{ BeforeAndAfter, OneInstancePerTest }

class ImagePullingSpec extends AnyFunSuite with BeforeAndAfter with OneInstancePerTest {
  test("Validate that when the 'skipPull' argument is passed in no imaged are pull from the Docker registry") {
    val instanceMock = new DockerComposePluginLocal with MockOutput

    instanceMock.pullDockerImages(Seq(skipPullArg), null, suppressColor = false)
    assert(instanceMock.messages.exists(_.contains("Skipping Docker Repository Pull for all images.")))
  }

  test("Validate that images with a 'build' source not pulled from the Docker registry") {
    val instanceMock = new DockerComposePluginLocal with MockOutput
    val imageName = "buildImageName"
    val serviceInfo = ServiceInfo("serviceName", imageName, buildImageSource, null)

    instanceMock.pullDockerImages(null, List(serviceInfo), suppressColor = false)
    assert(instanceMock.messages.contains(s"Skipping Pull of image: $imageName"))
  }

  test("Validate that images with a 'defined' source are pulled from the Docker registry") {
    val instanceMock = spy(new DockerComposePluginLocal)
    val imageName = "buildImageName"
    val serviceInfo = ServiceInfo("serviceName", imageName, definedImageSource, null)

    doNothing().when(instanceMock).dockerPull(imageName)

    instanceMock.pullDockerImages(null, List(serviceInfo), suppressColor = false)

    verify(instanceMock, times(1)).dockerPull(imageName)
  }

  test("Validate that images with a 'cache' source are not pulled from the Docker registry") {
    val instanceMock = new DockerComposePluginLocal with MockOutput
    val imageName = "cacheImageName"
    val serviceInfo = ServiceInfo("serviceName", imageName, cachedImageSource, null)

    instanceMock.pullDockerImages(null, List(serviceInfo), suppressColor = false)
    assert(instanceMock.messages.contains(s"Skipping Pull of image: $imageName"))
  }
}
