package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.CompanyRepository;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company handleCreateCompany(Company company) {
        return this.companyRepository.save(company);
    }

    public ResultPaginationDTO handleGetCompany(Specification<Company> spec, Pageable pageable) {
        Page<Company> pCompany = this.companyRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pCompany.getTotalPages());
        mt.setTotal(pCompany.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(pCompany.getContent());
        return rs;
    }

    public Company handleUpdateCompany(Company c) {
        Optional<Company> companyOptional = this.companyRepository.findById(c.getId());
        if (companyOptional.isPresent()) {
            Company currentCompany = companyOptional.get();
            currentCompany.setLogo(c.getLogo());
            currentCompany.setName(c.getName());
            currentCompany.setDescription(c.getDescription());
            currentCompany.setAddress(c.getAddress());
            return this.companyRepository.save(currentCompany);
        }
        return null;
    }

    public void handleDeleteCompany(long id) {
        this.companyRepository.deleteById(id);
    }
}
