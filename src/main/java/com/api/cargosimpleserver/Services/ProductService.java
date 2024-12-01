package com.api.cargosimpleserver.Services;

import com.api.cargosimpleserver.DTO.ProductDTO;
import com.api.cargosimpleserver.Exceptions.ProductAlreadyExistsException;
import com.api.cargosimpleserver.Exceptions.ProductNotFoundException;
import com.api.cargosimpleserver.Models.Product;
import com.api.cargosimpleserver.Repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для управления товарами.
 *
 * <p>
 * Предоставляет функциональность для создания, обновления, удаления и
 * получения информации о товарах.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final ModelMapper modelMapper;

    /**
     * Создание нового товара.
     *
     * <p>
     * Метод проверяет уникальность артикула товара,
     * преобразует DTO в модель, сохраняет её и возвращает
     * обратно в виде DTO.
     * </p>
     *
     * @param productDTO DTO товара для создания
     * @return DTO созданного товара
     * @throws ProductAlreadyExistsException если товар с таким артикулом уже существует
     */
    @Transactional
    public ProductDTO createProduct(ProductDTO productDTO) {

        Product product = modelMapper.map(productDTO, Product.class);

        validateProduct(product);

        Product savedProduct = productRepository.save(product);
        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    /**
     * Обновление существующего товара.
     *
     * <p>
     * Метод находит товар по ID, проверяет уникальность нового артикула,
     * обновляет данные и сохраняет изменения.
     * </p>
     *
     * @param id идентификатор обновляемого товара
     * @param productDTO DTO с новыми данными товара
     * @return DTO обновленного товара
     * @throws ProductNotFoundException если товар не найден
     * @throws ProductAlreadyExistsException если новый артикул уже существует
     */
    @Transactional
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {

        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Товар с ID " + id + " не найден"));

        if (!existingProduct.getArticleNumber().equals(productDTO.getArticleNumber())) {
            if (productRepository.existsByArticleNumber(productDTO.getArticleNumber())) {
                throw new ProductAlreadyExistsException("Товар с артикулом " +
                        productDTO.getArticleNumber() + " уже существует");
            }
        }

        modelMapper.map(productDTO, existingProduct);

        Product updatedProduct = productRepository.save(existingProduct);

        return modelMapper.map(updatedProduct, ProductDTO.class);
    }

    /**
     * Удаление товара по его идентификатору.
     *
     * @param id идентификатор товара для удаления
     */
    @Transactional
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    /**
     * Проверка уникальности артикула товара.
     *
     * <p>
     * Метод проверяет, не существует ли уже товар с таким же артикулом.
     * </p>
     *
     * @param product проверяемый товар
     * @throws ProductAlreadyExistsException если товар с таким артикулом уже существует
     */
    private void validateProduct(Product product) {
        if (productRepository.existsByArticleNumber(product.getArticleNumber())) {
            throw new RuntimeException("Товар с таким артикулом уже существует");
        }
    }

    /**
     * Получение списка всех товаров.
     *
     * @return список всех товаров в виде DTO
     */
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Получение товара по его идентификатору.
     *
     * @param id идентификатор товара
     * @return DTO товара
     * @throws ProductNotFoundException если товар не найден
     */
    public ProductDTO getProductById(Long id) {
        return productRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Товар не найден"));
    }

    /**
     * Конвертация сущности Product в DTO.
     *
     * @param product Сущность Product
     * @return DTO ProductDTO
     */
    private ProductDTO convertToDTO(Product product) {
        return modelMapper.map(product, ProductDTO.class);
    }
}