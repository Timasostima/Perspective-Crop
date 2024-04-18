import cv2
import matplotlib.pyplot as plt
# Read image
img = cv2.imread("C:/Users/Serious Tim/eclipse-workspace/Pruebas/imgs/example.jpg")
cv2.detailEnhance(img, sigma_s=10, sigma_r=0.15)
# img = cv2.resize(img, dsize=None, fx=0.5, fy = 0.5)
# plt.imshow(img[:,:,::-1])
# plt.show()

# sr = cv2.dnn_superres.DnnSuperResImpl_create()
 
# path = "C:/Users/Serious Tim/Documents/EDSR_x3.pb"
 
# sr.readModel(path)
 
# sr.setModel("edsr",3)
 
# result = sr.upsample(img)

# # Resized image
# # resized = cv2.resize(img,dsize=None,fx=3,fy=3)
# # cv2.imwrite("C:/Users/Serious Tim/eclipse-workspace/Pruebas/imgs/resized.jpg", resized)
# cv2.imwrite("C:/Users/Serious Tim/eclipse-workspace/Pruebas/imgs/result.jpg", result)


# sr2 = cv2.dnn_superres.DnnSuperResImpl_create()
# path2 = "C:/Users/Serious Tim/Documents/LapSRN_x8.pb"
# sr2.readModel(path2)
# sr2.setModel("lapsrn",8)
# result2 = sr2.upsample(img)
# cv2.imwrite("C:/Users/Serious Tim/eclipse-workspace/Pruebas/imgs/result2.jpg", result2)


sr3 = cv2.dnn_superres.DnnSuperResImpl_create()
path3 = "C:/Users/Serious Tim/Documents/FSRCNN_x4.pb"
sr3.readModel(path3)
sr3.setModel("fsrcnn",4)
result3 = sr3.upsample(img)
cv2.imwrite("C:/Users/Serious Tim/eclipse-workspace/Pruebas/imgs/result3.jpg", result3)


# plt.figure(figsize=(12,8))
# plt.subplot(1,3,1)
# # Original image
# plt.imshow(img[:,:,::-1])
# plt.subplot(1,3,2)
# # SR upscaled
# plt.imshow(result[:,:,::-1])
# plt.subplot(1,3,3)
# # OpenCV upscaled
# plt.imshow(resized[:,:,::-1])
# plt.show()