import { store } from '@/stores/store'
import { ZipFileHandler } from '@/utils/fileHandling/ZipFileHandler'

/**
 * This class provides some basic functionality for the factories.
 */
export class BaseFactory {
  /**
   * Returns the content of a file through the stored loading type.
   * @param path - Path to the file
   * @return Content of the file
   * @throws Error if the file could not be found
   */
  protected static async getFile(path: string): Promise<string> {
    if (store().state.localModeUsed) {
      if (store().state.zipModeUsed) {
        await new ZipFileHandler().handleFile(
          await this.getLocalFile(window.location.origin + '/results.zip')
        )
        return this.getFileFromStore(path)
      } else {
        return await (await this.getLocalFile(`/files/${path}`)).text()
      }
    } else if (store().state.zipModeUsed) {
      return this.getFileFromStore(path)
    } else if (store().state.singleModeUsed) {
      return store().state.singleFillRawContent
    }
    throw new Error('No loading type specified')
  }

  private static getFileFromStore(path: string): string {
    const index = Object.keys(store().state.files).find((name) => name.endsWith(path))
    if (index == undefined) {
      throw new Error(`Could not find ${path} in zip file.`)
    }
    const file = store().state.files[index]
    if (file == undefined) {
      throw new Error(`Could not load ${path}.`)
    }
    return file
  }

  /**
   * Returns the content of a file from the local files.
   * @param path - Path to the file
   * @return Content of the file
   * @throws Error if the file could not be found
   */
  protected static async getLocalFile(path: string): Promise<Blob> {
    const request = await fetch(path)
    console.log(request)

    if (request.status == 200) {
      return request.blob()
    } else {
      throw new Error(`Could not find ${path} in local files.`)
    }
  }
}
